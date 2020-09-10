# fetchingdatafromapi

Fetching data from api https://api.stackexchange.com/2.2/search?order=desc&sort=activity&intitle=perl&site=stackoverflow
Placing this fetched data into recycler view (Used card view for better UI) and show list view according to search tag


Note - Data we fetched from this api is unstructured means there are multiple key-values available for different items index so we need to call JSON object 
in our code and done parsing

Using Junit4 library for test cases

Multiple tag search at a time feature supported.
Using Picaso library for showing image from URL.
Native connction library used for connection on background thread.
