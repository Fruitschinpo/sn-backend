(ns sn-backend.db
  (:require [sn-backend.domain.movie :refer :all]
            [clojure.string :refer :all]
            [clojure.java.jdbc :refer :all :as jdbc])
  (:gen-class))

;; How to write queries 
;; https://github.com/clojure/java.jdbc
;; https://en.wikibooks.org/wiki/Clojure_Programming/Examples/JDBC_Examples
;; @TODO
;; Add the JDBC driver
;; Create a Movie Domain map/vector

;; Creates a global database variable.
;; earmuffs since it's global.
(let [db-host "localhost"
      db-port 3306
      db-name "sortnight"]
  (def *db* {:classname "com.mysql.jdbc.Driver"
           :subprotocol "mysql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           :user "phcr"
           :password "awt800"}))

;; get-genres : map : list
;; returns just the value of the genre key
(defn get-genres [row]
  (:genre row))

(defn get-genre-id [row]
  (:id row))

;; get-movie-genres : string -> sequence
;; get's all the genres of a movie, returns a sequence of genres
(defn get-movie-genres [id]
  (with-db-connection [db-con *db*]
    (let [rs  (jdbc/query db-con ["select genre from genre join (select genre_id from movie_genre where movie_id=?) as gm on (gm.genre_id = genre.id)" id])]
      (map get-genres rs))))

(defn get-movie-genre-ids [genres]
  (with-db-connection [db-con *db*]
    (map (fn [x]
           (jdbc/query db-con ["select id from genre where genre = ?" 
                               (lower-case x)])) 
         genres)))

(defn genre-in? [genre]
  (with-db-connection [db-con *db*]
    (let [result (jdbc/query ["select id from genre where genre= ?" (lower-case genre)])]
      (if (zero? (first result))
        true
        false))))

(defn create-movie-genre-relation [movie-id genre-ids]
  (with-db-connection [db-con *db*]
    (map (fn [x] (jdbc/insert! db-con :movie_genre
                              {:genre_id x
                               :movie_id movie-id}))
         genre-ids)))

;; create-movie : map -> Movie
;; takes a map of values for a movie
;; return a new Movie record.
(defn create-movie [row]
  (let [genres (get-movie-genres (:id row))
        m (->Movie (:id row)
                   (:title row)
                   (:year row)
                   (:runtime row)
                   genres)]
    m))

;; Test code for the database.
;; Will be removed later.
(defn list-all []
  (with-db-connection [db-con *db*]
    (let [rs (jdbc/query db-con ["select * from movie"])]
      (map create-movie rs))))


;; add-movie : map -> boolean
;; adds a movie to the database to persist it.
;; returns a boolean if it was successfull or not.
(defn add-movie [movie]
  (with-db-transaction [db-con *db*]
    (println "MOVIE" movie)
    (let [movie-id (jdbc/insert! db-con :movie
                                 {:title (:title movie)
                                  :year (:year movie)
                                  :runtime (:runtime movie)})
          genre-ids (get-movie-genre-ids (:genres movie))]
      (println "GENRES" genre-ids)
      (create-movie-genre-relation movie-id genre-ids))))


;; search-movie : map -> movies
;; searches the database for movies with the attributes in the search.
;; returns the result of all the movie found.
(defn search-movie [attributes])

;; delete-movie : string -> boolean
;; delets the movie in the database with the id supplied.
;; returns a boolean if it was successfull or not.
(defn delete-movie [id])

;; create-user-account : user -> boolean
;; takes information about a user and adds them to the database
;; returns a boolean if it was successfull or not.
(defn create-user-account [user])


;; delete-user-account : string -> boolean
;; removes a user from the database
;; returns a boolean if it was successfull or not.
(defn delete-user-account [id])


;; add-user-to-group : string, string -> boolean
;; takes a user id and group id and creates a relation 
;; between the user and the group, so the user belongs to the group.
(defn add-user-to-group [user, group])
