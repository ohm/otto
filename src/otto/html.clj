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
                 [:a {:href (format "/%s" n)} n]])) organizations)])

(defn- repository-collection
  [repositories]
  (if (empty? repositories)
    [:p "Listing currently unavailable."]
    [:ul (for [[_ repository-data] repositories]
           [:li (:name repository-data)])]))

(defn organization-view
  [organizations organization repositories]
  (layout [:body (organization-collection organizations organization)
                 (repository-collection repositories)]))
