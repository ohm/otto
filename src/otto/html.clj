(ns otto.html
  (:require [hiccup.core    :as core    :refer [h]]
            [hiccup.element :as element :refer [link-to]]
            [hiccup.page    :as page    :refer [html5 include-css include-js]]))

(defn- navigation
  [organizations organization]
  [:div.navbar.navbar-inverse.navbar-fixed-top {:role "navigation"}
   [:div.container-fluid
    [:div.navbar-header
     [:span.navbar-brand "Repositories"]]
    [:div.collapse.navbar-collapse
     [:ul.nav.navbar-nav (map (fn [o]
                                (let [n (:name o)]
                                  [:li (if (= o organization)
                                         {:class "active"})
                                    (link-to (format "/%s" n) n)]))
                              organizations)]
     [:form#filters.navbar-form {:role "form"}
      [:div.checkbox
       [:label.checkbox-inline.navbar-link
        [:input {:checked true :type "checkbox" :value "fork"}]
        "&nbsp;Forks"]]
      [:div.checkbox
       [:label.checkbox-inline.navbar-link
        [:input {:checked true :type "checkbox" :value "private"}]
        "&nbsp;Private"]]
      [:div.checkbox
       [:label.checkbox-inline.navbar-link
        [:input {:checked true :type "checkbox" :value "public"}]
        "&nbsp;Public"]]]]]])

(defn- format-string
  [string]
  (if (clojure.string/blank? string)
    "&ndash;"
    (h string)))

(defn- format-date
  [date]
  (if (nil? date)
    "&ndash;"
    (.format (java.text.SimpleDateFormat. "dd-MM-yyyy") date)))

(defn- repository-data-attributes
  [fork private]
  {:data-fork    (format "%s" fork)
   :data-private (format "%s" private)})

(defn- repository-detail
  [repository]
  (let [fork    (:fork repository)
        private (:private repository)]
    [:tr (merge (if private {:class "active"}
                            {}) (repository-data-attributes fork private))
     [:td (link-to (:html_url repository) (:name repository))]
     [:td (format-string (:description repository))
          "&nbsp;"
          (if (= true fork)
            [:span {:class "label label-default"} "fork"])]
     [:td (format-string (:language repository))]
     [:td.date (format-date (:pushed_at repository))]]))

(defn- repository-collection
  [repositories]
  (if (empty? repositories)
    [:div.alert.alert-danger "Listing currently unavailable."]
    [:table.table.table-condensed
     [:thead
      [:tr
       [:th.name "Name"]
       [:th.description "Description"]
       [:th.language "Language"]
       [:th.date "Date"]]]
     [:tbody (for [[_ repository] repositories]
               (repository-detail repository))]]))

(defn organization-view
  [organizations organization repositories]
  (html5 [:head
          [:title (format "[%s] Repositories" (:name organization))]
          (include-css "bootstrap.min.css" "otto.css")
          (include-js "jquery.min.js" "otto.js")]
         [:body
          [:div.container-fluid
           (navigation organizations organization)
           (repository-collection repositories)]]))
