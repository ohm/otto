(ns otto.html
  (:require [hiccup.page :as hiccup]))

(defn- organization-navigation
  [organizations organization]
  [:ul {:class "nav nav-tabs"}
   (map (fn [o]
          (let [n (:name o)]
            [:li (if (= o organization)
                   {:class "active"})
             [:a {:href (format "/%s" n)} n]])) organizations)])

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
    [:table {:class "table table-condensed"}
     [:thead [:tr [:th "Name"]
                  [:th "Description"]
                  [:th "Language"]
                  [:th "Date"]]]
     [:tbody (for [[_ repository] repositories]
               [:tr (if (= true (:private repository))
                      {:class "active"})
               [:td [:a {:href (:html_url repository)} (:name repository)]]
               [:td (format-string (:description repository))
                    "&nbsp;"
                    (if (= true (:fork repository))
                      [:span {:class "label label-default"} "fork"])]
               [:td (format-string (:language repository))]
               [:td {:class "date"}
                (format-date (:pushed_at repository))]])]]))

(defn organization-view
  [organizations organization repositories]
  (hiccup/html5 [:head [:link {:href "/bootstrap.min.css"
                               :rel  "stylesheet"
                               :type "text/css"}]
                       [:link {:href "/otto.css"
                               :rel  "stylesheet"
                               :type "text/css"}]]
                [:body [:div {:class "container-fluid"}
                        [:div {:class "page-header"}
                         [:h1 "Repositories"]]
                        (organization-navigation organizations organization)
                        (repository-collection repositories)]]))
