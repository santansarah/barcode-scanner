package com.santansarah.barcodescanner.data.remote.mock

import com.santansarah.barcodescanner.data.paging.ProductSearchPagingSource
import com.santansarah.barcodescanner.data.remote.SearchProductItem

/**
 * Here's a real JSON response that comes back for corn chips. The Api returns the:
 * total product count, current page, page_count - which is how many products
 * are ON this specific page, and then page_size - the max products that are
 * returned per page. Then, I get a list of products, which are stored in
 * [SearchProductItem]. For Paging 3, I use the metadata to determine my
 * paging functionality, and the [SearchProductItem] as the LoadResult. Now,
 * let's go back to [ProductSearchPagingSource] and see how all of this comes
 * together.
 */
const val searchResults = """{
   "count":119,
   "page":1,
   "page_count":24,
   "page_size":24,
   "products":[
      {
         "brand_owner":"Wal-Mart Stores, Inc.",
         "code":"0078742081304",
         "image_front_small_url":"https://images.openfoodfacts.org/images/products/007/874/208/1304/front_en.7.200.jpg",
         "product_name":"Corn Chips"
      },
      {
         "code":"07871122",
         "image_front_small_url":"https://images.openfoodfacts.org/images/products/07871122/front_en.8.200.jpg",
         "product_name":"Hot nacho cheese tortilla chips naturally flavored"
      },
      {
         "brand_owner":"Wal-Mart Stores, Inc.",
         "code":"0078742083049",
         "product_name":"Great value, potato chips, burnin hot"
      },
      {
         "brand_owner":"Wal-Mart Stores, Inc.",
         "code":"0078742093208",
         "image_front_small_url":"https://images.openfoodfacts.org/images/products/007/874/209/3208/front_en.4.200.jpg",
         "product_name":"Glacier Ranch tortilla chips"
      },
      {
         "code":"0078742276175",
         "image_front_small_url":"https://images.openfoodfacts.org/images/products/007/874/227/6175/front_en.17.200.jpg",
         "product_name":"Cheddar sour cream"
      },
      {
         "brand_owner":"Wal-Mart Stores, Inc.",
         "code":"0078742021997",
         "image_front_small_url":"https://images.openfoodfacts.org/images/products/007/874/202/1997/front_en.4.200.jpg",
         "product_name":"Kettle Cooked Potato Chips"
      },
      {
         "brand_owner":"GOOD & GATHER",
         "code":"0085239042670",
         "image_front_small_url":"https://images.openfoodfacts.org/images/products/008/523/904/2670/front_en.6.200.jpg",
         "product_name":"Organic White Corn Tortilla Chips"
      }
   ],
   "skip":0
}"""