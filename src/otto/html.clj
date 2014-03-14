(ns otto.html
  (:require [hiccup.page :refer [html5]]))

(defn- layout
  [body]
  (html5 body))

(defn- repository-collection
  [repositories]
  (if (empty? repositories)
    [:p "Listing currently unavailable."]
    [:ul (for [[k _] repositories]
           [:li k])]))

(defn organization-view
  [repositories]
  (layout (repository-collection repositories)))
