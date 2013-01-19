REM java -cp lib/hsqldb.jar org.hsqldb.Server -database.0 file:db/swazamdb -dbname.0 SWAZAMDB
REM java -cp lib/hsqldb.jar org.hsqldb.Server -database.0 file:db/swazamserverdb -dbname.0 SWAZAMSERVERDB
java -cp lib/hsqldb.jar org.hsqldb.Server -database.0 file:db/swazamdb -dbname.0 SWAZAMDB -database.1 file:db/swazamserverdb -dbname.1 SWAZAMSERVERDB