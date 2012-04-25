package todo

import (
    "fmt"
    "appengine"
    "appengine/user"
    "appengine/datastore"
    "html/template"
    "net/http"
    "time"
    "strings"
)

type TodoPageData struct {
    Username string
    LoginUrl string
    TodoItems []TodoItem
}
type TodoItem struct {
    Title string //Must be capitalized, meens public/exposed.
    Body  string
    Done  string //Values: "checked" or ""
    Created  time.Time
}

func init() {
    http.HandleFunc("/", root)
    http.HandleFunc("/sign", sign)
    http.HandleFunc("/setdone", setdone)
}

func setdone(w http.ResponseWriter, r *http.Request) {
//TODO: Update DB and return OK
//    c := appengine.NewContext(r)
    val := r.RequestURI
    q := strings.Index(val,"?")
    if (q > 0) {
        d := val[(q+1):]
        fmt.Fprint(w, "argument ", d)
    } else {
        fmt.Fprint(w, "argument missing")
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
    handleErr(w,err)
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
    item := TodoItem{r.FormValue("title"), r.FormValue("body"), "checked", time.Now()}
    _, err := datastore.Put(c, datastore.NewIncompleteKey(c, u.String(), nil), &item)
    handleErr(w,err)
    http.Redirect(w, r, "/", http.StatusFound) 
}

func handleErr(w http.ResponseWriter,e error) {
    if e != nil {
        http.Error(w, e.Error(), http.StatusInternalServerError)
    }
}

