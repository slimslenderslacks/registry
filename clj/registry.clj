(ns registry
  (:require [babashka.curl :as curl]
            [cheshire.core :as json]))

(defn fetch-servers [{:keys [cursor]}]
  (->
   (curl/get
    "http://localhost:8080/v0/servers"
    {:query-params (merge {:limit 100}
                          (when cursor {:cursor cursor}))})
   :body
   (json/parse-string true)))

(defn server-detail [{:keys [id name version_detail]}]
  (->
   (curl/get
    (format "http://localhost:8080/v0/servers/%s" id)
    {})
   :body
   (json/parse-string true)))

(def servers
  (loop [agg [] opts {}]
    (let [{:keys [servers metadata]} (fetch-servers opts)]
      (if-let [next-cursor (:next_cursor metadata)]
        (recur (concat agg (map server-detail servers)) {:cursor next-cursor})
        (concat agg (map server-detail servers))))))

