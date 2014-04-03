(ns otto.html
  (:require [hiccup.page :refer [html5]]))

(defn- layout
  [body]
  (html5 body))

(defn- organization-collection
  [organizations current]
  [:ul (map (fn [o]
              (let [n (:name o)]
                [:li (if (= o current)
                       {:class "current"})
                 [:a {:href (format "/%s" n)} n]]))
            organizations)])

(defn- format-string
  [string]
  (if (clojure.string/blank? string)
    "&ndash;"
    string))

(defn- format-date
  [date]
  (if (nil? date)
    "&ndash;"
    (.format (java.text.SimpleDateFormat. "dd-MM-yyyy") date)))

(defn- repository-collection
  [repositories]
  (if (empty? repositories)
    [:p "Listing currently unavailable."]
    [:table
     [:thead [:tr [:th "Name"]
                  [:th "Description"]
                  [:th "Language"]
                  [:th "Date"]]]
     [:tbody (for [[_ repository] repositories]
               [:tr (if (= true (:private repository))
                     {:class "private"})
               [:td [:a {:href (:html_url repository)} (:name repository)]]
               [:td (format-string (:description repository))
                    (if (= true (:fork repository))
                      [:span {:class "fork"} "fork"])]
               [:td (format-string (:language repository))]
               [:td (format-date (:pushed_at repository))]])]]))

(defn organization-view
  [organizations organization repositories]
  (layout [:body (organization-collection organizations organization)
                 (repository-collection repositories)]))
