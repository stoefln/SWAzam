@echo off
start hsqldb_start.bat
start start_jade.bat
PING -n 6 127.0.0.1>nul 
start start_peer_agent.bat
PING -n 6 127.0.0.1>nul
start start_server_Agent.bat
PING -n 6 127.0.0.1>nul
start start_client_gui.bat