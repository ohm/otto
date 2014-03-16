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
           [:li (:name repository-data)])]))

(defn organization-view
  [repositories]
  (layout (repository-collection repositories)))
