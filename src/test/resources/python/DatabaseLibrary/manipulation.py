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

class Manipulation(object):
    """
    Manipulation handles data manipulation. 
    """

    def execute_sql(self, operation, *parameters):
        """
        Executes the SQL statement given in `operation`, passing in the given 
        parameters, which could be used by prepared statements. If the statement
        yields a result, it is returned.
        
        Example:
        Simple update statement with parameters as part of the operation string.
        | Execute Sql | UPDATE MYTABLE set FLAG = ${accepted} WHERE ID=${id} |
        
        The syntax for parameterized statements is database dependent. Search 
        Google for "dbapi2 cheat sheet" if you want to pass parameters.
        """
        cur = None
        try:
            cur = self._dbconnection.cursor()
            cur.execute (operation);
            if(cur.description is not None):
                allRows = cur.fetchall()
                return allRows
        finally :
            if cur :
                cur.close() 
        


