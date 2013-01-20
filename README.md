SWAzam
======

A distributed music crawler based on the JADE framework:
http://jade.tilab.com/

Installing SWAzam
--------------
Just open the project in eclipse and build it. It includes everything you will need.

Running SWAzam
--------------

1. start script ./start_jade.sh
2. start script ./start_server_agent.sh
3. start script ./start_peer_agent.sh
4. start script ./start_client_gui.sh

You can start more peers with

./start_peer_agent.sh [someUniqueNumber]

Running Fingerpint Test Application
--------------
1. Run Main as Console Application
2. Check console for the output

Start HSQLDB [ Configuration file - persistence.xml ]
--------------
Run hsqldb_start.bat

Start HSQLDB Manager
--------------
1. Run hsqldb_manager.bat
2. Select Type "HSQL Database Engine Server"
3. Change URL to "jdbc:hsqldb:hsql://localhost/swazamdb"
4. Click "Ok"
