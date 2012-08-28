#  Copyright (c) 2010 Franz Allan Valencia See
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

import ConfigParser

class ConnectionManager (object):
    """
    Connection Manager handles the connection & disconnection to the database.
    """

    def __init__(self):
        """
        Initializes _dbconnection to None.
        """
        self._dbconnection = None
        

    def connect_to_dbapi2(self, dbapiModuleName, *args):
        """
        Loads the DB API 2.0 module with the given `dbapiModuleName`, then uses 
        it to connect to the database using `args`. The arguments depend on the 
        dbapiModule which is used for the connection.
        
        Example usage:
        | # Using psycopg2, specify postgres database name, user and password |
        | Connect To Dbapi2 | psycopg2 | my_db | user | s3cr3t |
        
        | # Using pymysql, specify mysql database host, user, password, db and port |
        | Connect To Dbapi2 | pymysql | localhost | user | s3cr3t | my_db | 3306 |

        | # Using jaydebeapi, set classpath, connect via Oracle jdbc driver in Python or Jython |
        | Set Environment Variable | CLASSPATH | ${CURDIR}/../libraries/ojdbc14-10.2.0.3.0.jar |
        | Connect To Dbapi2 | jaydebeapi | oracle.jdbc.driver.OracleDriver | jdbc:oracle:thin:@host:port | user | s3cr3t |

        | # Using jaydebeapi, set classpath, connect via Firebird jdbc driver in Python or Jython |
        | Set Environment Variable | CLASSPATH | ${CURDIR}/../libraries/jaybird-full-2.1.6.jar
        | Connect To Dbapi2	| jaydebeapi | org.firebirdsql.jdbc.FBDriver | jdbc:firebirdsql:localhost/3050:c:/data/test.fdb | SYSDBA | masterkey |

        """
        db_api_2 = __import__(dbapiModuleName);
        # TODO: pass configfile as second arg dbConfigFile="./resources/db.cfg"? 
        # maybe use ConfigParser.SafeConfigParser({'bar': 'Life', 'baz': 'hard'})
        # seems to be recommended by Python reference
        # don't know how to handle config.items so that it can be passed as args
        self._dbconnection = db_api_2.connect (*args);              
        
    def connect_to_database(self, dbapiModuleName=None, dbName=None, dbUsername=None, dbPassword=None, dbConfigFile="./resources/db.cfg"):
        """
        Loads the DB API 2.0 module given `dbapiModuleName` then uses it to 
        connect to the database using `dbName`, `dbUsername`, and `dbPassword`.
        
        Optionally, you can specify a `dbConfigFile` wherein it will load the
        default property values for `dbapiModuleName`, `dbName` `dbUsername` 
        and `dbPassword` (note: specifying `dbapiModuleName`, `dbName` 
        `dbUsername` or `dbPassword` directly will override the properties of
        the same key in `dbConfigFile`). If no `dbConfigFile` is specified, it
        defaults to `./resources/db.cfg`. 
        
        The `dbConfigFile` is useful if you don't want to check into your SCM
        your database credentials.
        
        Example usage:
        | # explicitly specifies all db property values |
        | Connect To Database | psycopg2 | my_db | postgres | s3cr3t | 

        | # loads all property values from default.cfg |
        | Connect To Database | dbConfigFile=default.cfg | 
        
        | # loads all property values from ./resources/db.cfg |
        | Connect To Database |  
        
        | # uses explicit `dbapiModuleName` and `dbName` but uses the `dbUsername` and `dbPassword` in 'default.cfg' |
        | Connect To Database | psycopg2 | my_db_test | dbConfigFile=default.cfg |
        
        | # uses explicit `dbapiModuleName` and `dbName` but uses the `dbUsername` and `dbPassword` in './resources/db.cfg' |
        | Connect To Database | psycopg2 | my_db_test |    
        """
    
        config = ConfigParser.ConfigParser()
        config.read([dbConfigFile])
        
        dbapiModuleName = dbapiModuleName or config.get('default', 'dbapiModuleName')
        dbName = dbName or config.get('default', 'dbName')
        dbUsername = dbUsername or config.get('default', 'dbUsername')
        dbPassword = dbPassword or config.get('default', 'dbPassword')
        
        db_api_2 = __import__(dbapiModuleName);
        self._dbconnection = db_api_2.connect (database=dbName, user=dbUsername, password=dbPassword)
        
    def disconnect_from_database(self):
        """
        Disconnects from the database.
        
        For example:
        | Disconnect From Database | # disconnects from current connection to the database | 
        """
        self._dbconnection.close()
        
