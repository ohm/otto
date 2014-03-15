(ns otto.html
  (:require [hiccup.page :refer [html5]]))

(defn- layout
  [body]
  (html5 body))

(defn- repository-collection
  [repositories]
  (if (empty? repositories)
    [:p "Listing currently unavailable."]
    [:ul (for [[_ repository-data] repositories]
           [:li (get repository-data "name")])]))

(defn organization-view
  [repositories]
  (layout (repository-collection repositories)))
