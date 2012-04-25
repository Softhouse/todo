from wsgiref.simple_server import make_server
import urlparse
import todo
import json




def todo_app(environ, start_response):


    path = environ['PATH_INFO']    
    method = environ['REQUEST_METHOD']
    query = urlparse.parse_qs(environ['QUERY_STRING'])
    
    resource = path.split("/")[1]

    todos = todo.load_todo(resource)
    if method == 'GET':        
        request_body = json.dumps([t._asdict() for t in todos])
    elif method == 'POST':
        todo.Todo(**query)
        todos = [todo] + todos
        todo.save_todo(path, todos)
    elif method == 'PUT':
        index = int(query.pop('index'))
        todos[index] = todos[index]._replace(**query)
        todo.save_todo(path, todos)
    elif method == 'DELETE':
        index = int(query.pop('index'))
        del todos[index]
        todo.save_todo(path, todos)

    print resource
    print path
        


    status = b'200 OK' # HTTP Status
    headers = [(b'Content-type', b'text/plain; charset=utf-8')] # HTTP Headers
    start_response(status, headers)
    # The returned object is going to be printed
    return [request_body]

httpd = make_server('', 8000, todo_app)

# Serve until process is killed
httpd.serve_forever()
