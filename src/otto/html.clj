(ns otto.html
  (:require [hiccup.element :as element :refer [link-to]]
            [hiccup.page    :as page    :refer [html5 include-css]]))

(defn- organization-navigation
  [organizations organization]
  [:ul.nav.nav-tabs
   (map (fn [o]
          (let [n (:name o)]
            [:li (if (= o organization)
                   {:class "active"})
             (link-to (format "/%s" n) n)])) organizations)])

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
    [:div.alert.alert-danger "Listing currently unavailable."]
    [:table.table.table-condensed
     [:thead
      [:tr
       [:th.name        "Name"]
       [:th.description "Description"]
       [:th.language    "Language"]
       [:th.date        "Date"]]]
     [:tbody
      (for [[_ repository] repositories]
        [:tr (if (= true (:private repository))
               {:class "active"})
         [:td (link-to (:html_url repository) (:name repository))]
         [:td (format-string (:description repository))
              "&nbsp;"
              (if (= true (:fork repository))
                [:span {:class "label label-default"} "fork"])]
         [:td (format-string (:language repository))]
         [:td.date (format-date (:pushed_at repository))]])]]))

(defn organization-view
  [organizations organization repositories]
  (html5 [:head
          (include-css "bootstrap.min.css" "otto.css")]
         [:body
          [:div.container-fluid
           [:div.page-header
            [:h1 "Repositories"]]
           (organization-navigation organizations organization)
           (repository-collection repositories)]]))
