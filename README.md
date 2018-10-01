# TRENDING MOVIES

![Build status](https://app.bitrise.io/app/51efa45b426c92e7/status.svg?token=OIYud8bE68dyPlD9h8aMgQ&branch=master)

Demo project built over **theMovieDb** API.

Build with `android architecture components`, clean architecture and designed with **Material design**

Main goals on this project:
 
* Easy to understand some concepts from Clean Architecture.
* Applies & follows design principles.
* Layer separation by packages to set clear boundaries between each layer.
* Easy to read, scale, test and maintain.
 
## Quick overview of the app

Code of this app is divided in packages, following clean architecture:

* data
    * cache
    * network
    
Classes related to framework and data, network connectivity, storage, ... 
    
* domain
    * repository
    * usecase
    
Business logic of the app
    
* ui
    * detail
    * movies
    * search
    * splash
    
Where screens belong
 
**API endpoints**
 
* https://api.themoviedb.org/3/movie/popular
* https://api.themoviedb.org/3/search/movie
* https://api.themoviedb.org/3/movie/{movie_id}/similar
* https://api.themoviedb.org/3/movie/{movie_id}
 
* https://api.themoviedb.org/3/configuration
 
* https://api.themoviedb.org/3/genre/movie/list


**Libraries**
 
* Retrofit [http://square.github.io/retrofit/](http://square.github.io/retrofit/)
* OkHttp [http://square.github.io/okhttp/](http://square.github.io/okhttp/)
    * OkHttpInterceptor [https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
    * Chuck interceptor [https://github.com/jgilfelt/chuck](https://github.com/jgilfelt/chuck)
* Gson [https://github.com/google/gson](https://github.com/google/gson)
* Dagger2 [https://github.com/google/dagger](https://github.com/google/dagger)

**Testing libraries**

* Mockito [https://github.com/nhaarman/mockito-kotlin](https://github.com/nhaarman/mockito-kotlin)
* Espresso [https://developer.android.com/training/testing/espresso/](https://developer.android.com/training/testing/espresso/)
* Barista [https://github.com/SchibstedSpain/Barista](https://github.com/SchibstedSpain/Barista)


**Screens**
 
*Movies screen*: Scrollable list of movies, in a material design card style
 
*Movie detail*: Screen with big image, movie description and similar movies
 
*Search screen*: Input text box to search for movies, not search while typing
 
### TODO

- [ ] Change *dagger* to [*koin*](https://github.com/InsertKoinIO/koin)
- [ ] Refactor support libraries to [*androidX*](https://developer.android.com/topic/libraries/support-library/refactor)
- [ ] Create tablet mode
- [ ] Split in modules instead of packages 