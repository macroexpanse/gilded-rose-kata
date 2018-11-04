(ns gilded-rose.core-spec
  (:require [clojure.test :refer :all]
            [gilded-rose.core :refer [update-quality
                                      update-sell-in
                                      item
                                      sulfuras
                                      elixir-of-the-mongoose
                                      backstage-passes
                                      conjured
                                      aged-brie]]))

(defn update-item [item]
  (-> item
      update-sell-in
      update-quality))

(testing "Default"
  (deftest quality-dec-1-by-default
    (let [result (update-item (item elixir-of-the-mongoose 5 10))]
      (is (= 9 (result :quality)))))

  (deftest quality-dec-2-if-past-sell-in
    (let [result (update-item (item elixir-of-the-mongoose 1 10))]
      (is (= 8 (result :quality)))))

  (deftest sell-in-dec-1
    (let [result (update-item (item elixir-of-the-mongoose 1 10))]
      (is (= 0 (result :sell-in))))))

(testing "Aged Brie"
  (deftest quality-inc-1
    (let [result (update-item (item aged-brie 5 10))]
      (is (= 11 (result :quality)))))

  (deftest quality-max-50-aged-brie
    (let [result (update-item (item aged-brie 5 50))]
      (is (= 50 (result :quality))))))

(testing "Sulfuras, Hand of Ragnaros"
  (deftest quality-and-sell-in-dec-0
    (let [result (update-item (item sulfuras 0 80))]
      (is (= 80 (result :quality)))
      (is (= 0 (result :sell-in))))))

(testing "Backstage passes"
  (deftest quality-inc-1-when-sell-in-more-than-10
    (let [result (update-item (item backstage-passes 12 20))]
      (is (= 21 (result :quality)))))

  (deftest quality-inc-2-when-sell-in-10-or-less
    (let [result (update-item (item backstage-passes 11 20))]
      (is (= 22 (result :quality)))))

  (deftest quality-inc-3-when-sell-in-5-or-less
    (let [result (update-item (item backstage-passes 6 20))]
      (is (= 23 (result :quality)))))

  (deftest quality-0-if-sell-in-0
    (let [result (update-item (item backstage-passes 1 20))]
      (is (= 0 (result :quality)))))

  (deftest quality-max-50-backstage-passes
    (let [result (update-item (item backstage-passes 10 50))]
      (is (= 50 (result :quality))))))

(testing "Conjured"
  (deftest quality-dec-2
    (let [result (update-item (item conjured 20 20))]
      (is (= 18 (result :quality))))))

