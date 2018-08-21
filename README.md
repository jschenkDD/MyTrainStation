# MyTrainStation

This app will use free graphql endpoint of [Deutsche Bahn](https://developer.deutschebahn.com/free1bahnql/graphql) to search 
for stations that are near by current gps location and within specified search radius. This only works if permission 
`Manifest.permission.ACCESS_FINE_LOCATION` was granted.

Alternative you could use search term to find stations you are interested in.

**TODOs:**
- Show SnackBar if user denied permission
- load images of stations via AsyncTask and store them as blob in database (for example second table called `resource_table`) and 
link them as foreign key in existing table `train_stations`
- add `About` entry within app settings to show general information about the app
- think about using Dependency Injection (Dagger2)
