(ns gilded-rose.core)

(def sulfuras "Sulfuras, Hand Of Ragnaros")
(def elixir-of-the-mongoose "Elixir of the Mongoose")
(def backstage-passes "Backstage passes to a TAFKAL80ETC concert")
(def aged-brie "Aged Brie")
(def dexterity-vest "+5 Dexterity Vest")
(def conjured "Conjured")

(defmulti update-sell-in
  (fn [item] (item :name)))

(defmethod update-sell-in sulfuras [item]
  item)

(defmethod update-sell-in :default [item]
  (update-in item [:sell-in] dec))

(defmulti update-quality
  (fn [item] (item :name)))

(defn update-max-50 [f]
  (fn [x] (min (f x) 50)))

(defmethod update-quality aged-brie [item]
  (update-in item [:quality] (update-max-50 inc)))

(defmethod update-quality sulfuras [item]
  item)

(defmethod update-quality backstage-passes [item]
  (update-in item [:quality]
             (update-max-50
               (fn [quality]
                 (cond
                   (= (item :sell-in) 0)
                     0
                   (<= (item :sell-in) 5)
                     (+ quality 3)
                   (<= (item :sell-in) 10)
                     (+ quality 2)
                   :else
                     (+ quality 1))))))

(defmethod update-quality conjured [item]
  (update-in item [:quality] (comp dec dec)))

(defmethod update-quality :default [item]
  (update-in item [:quality]
             (fn [quality]
               (if (> (item :sell-in) 0)
                 (- quality 1)
                 (- quality 2)))))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory[]
  (let [inventory
    [
      (item dexterity-vest 10 20)
      (item aged-brie 2 0)
      (item elixir-of-the-mongoose 5 7)
      (item sulfuras 0 80)
      (item backstage-passes 15 20)
      (item conjured 20 20)
    ]]

    (map
      (fn [item]
        (-> item
            update-sell-in
            update-quality)) inventory)))

