package todo

import (
    "appengine"
    "appengine/user"
    "appengine/datastore"
    "html/template"
    "net/http"
    "time"
    "strconv"
)

type TodoPageData struct {
    Username string
    LoginUrl string
    TodoItems []TodoItem
}
type TodoItem struct {
    Title string //Must be capitalized, means public/exposed.
    Body  string
    Done  string //Values: "checked" or ""
    Created int64
}

func init() {
    http.HandleFunc("/", root)
    http.HandleFunc("/sign", sign)
    http.HandleFunc("/ajax", ajax)
}

func ajax(w http.ResponseWriter, r *http.Request) {
    c := appengine.NewContext(r)
    u := user.Current(c);
    if u == nil {
        c.Debugf("Not logged in")
        return
    }
    r.ParseForm() //Must call this to get POST and PUT parameters
//    meth := r.Method
    created := r.FormValue("created")
    checked := r.FormValue("checked")
    cre, err := strconv.ParseInt(created, 10, 64)
    if err != nil {
        c.Debugf("Err: ", err)
        return
    }
    key := datastore.NewKey(c, u.String(), "", cre, nil)
    var todo TodoItem
    if err := datastore.Get(c, key, &todo); err != nil {
        c.Debugf("Err: ", err)
        return
    }

    todo.Done = checked
    key, err = datastore.Put(c, key, &todo)
    if err != nil {
        c.Debugf("Err: ", err)
        return
    }
}

func root(w http.ResponseWriter, r *http.Request) {
    c := appengine.NewContext(r)
    pageData := TodoPageData{}

    if u := user.Current(c); u == nil {
        pageData.Username = "Not logged in"
        url, err := user.LoginURL(c, r.URL.String())
        handleErr(w,err)
        pageData.LoginUrl = url
    } else {
        pageData.Username = u.String()
        url, err := user.LogoutURL(c, r.URL.String())
        handleErr(w,err)
        pageData.LoginUrl = url
    }

    q := datastore.NewQuery(pageData.Username).Order("-Created").Limit(10)
    //make is like a constructor
    //[] is a "slice", ref counted, dynamic array
    items := make([]TodoItem, 0, 10)

    _, err := q.GetAll(c, &items)
    pageData.TodoItems = items

    templ, err := template.ParseFiles("todo.html")
    handleErr(w,err)
    err = templ.Execute(w, pageData) // populate html page with data from pageData
    handleErr(w,err)
}

func sign(w http.ResponseWriter, r *http.Request) {
    c := appengine.NewContext(r)
    u := user.Current(c);
    if u == nil {
        http.Error(w, "Must be logged in", http.StatusInternalServerError)
        return
    }
    now := time.Now().Unix()
    item := TodoItem{r.FormValue("title"), r.FormValue("body"), "", now}
    key, err := datastore.Put(c, datastore.NewKey(c, u.String(), "", now, nil), &item)
    handleErr(w,err)
    http.Redirect(w, r, "/", http.StatusFound) 
}

func handleErr(w http.ResponseWriter,e error) {
    if e != nil {
        http.Error(w, e.Error(), http.StatusInternalServerError)
    }
}

