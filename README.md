# sn-backend

The sortnight backend, designed to handle requests and add movies to a database.

## Usage

### How ro run
You need to install [leiningen](http://leiningen.org/)
You will also need to install [Clojure](http://clojure.org/getting_started)

Once you have installed both of them you will run the server by doing:
(In the terminal, might be different in windows. This is how you would do it in linux/mac)
```
lein repl
user=> (use 'ring.adapter.jetty)
user=> (use 'sn-backend.core)
user=> (run-jetty #'app {:port 3000})
```

You can then test to go to http://localhost:3000 and see that the server is running.

You did it!

## License

Copyright © 2015 SortNight

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
