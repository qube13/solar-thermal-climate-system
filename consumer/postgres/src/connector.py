#!/usr/bin/env python3

import logging

import psycopg2
import psycopg2.extras

from datatypes import data_types

__author__ = "Stephan Müller"
__license__ = "MIT"


logger = logging.getLogger(__name__)


class PostgresConnector:

    def __init__(self, host, port, database, user, password):
        try:
            self.con = psycopg2.connect(host=host, port=port, database=database, user=user, password=password)
            self.cur = self.con.cursor()
            # self.cur = self.con.cursor(cursor_factory=psycopg2.extras.DictCursor)
        except Exception as e:
            raise e

    def table_exists(self, table_name):
        sql = self.cur.mogrify("""SELECT TRUE
                  FROM information_schema.tables
                  WHERE table_name=%s""", (table_name,))
        self.cur.execute(sql)

        result = self.cur.fetchall()
        if len(result) == 1:
            return True
        return False

    def create_table(self, table_name, data):

        # todo table_name = re.sub('[^A-Za-z0-9]+', '', table_name)

        column_names = list()
        column_sql = ""
        for key in data:
            column_names.append(key)

            try:
                data_type = data_types[type(data[key])]
            except KeyError:
                data_type = data_types[str]

            column_sql += ", " + key + " " + data_type

        sql = """CREATE TABLE IF NOT EXISTS %s (
                    timestamp TIMESTAMP PRIMARY KEY DEFAULT current_timestamp
                    %s
                );""" % (table_name, column_sql)

        try:
            self.cur.execute(sql)
            self.con.commit()

        except psycopg2.DatabaseError as de:
            logger.error(de.pgcode + " " + de.pgerror.replace("\n", " "))

    def update_columns(self, table_name, data):

        # todo table_name = re.sub('[^A-Za-z0-9]+', '', table_name)
        sql = self.cur.mogrify("""SELECT column_name
                  FROM information_schema.columns
                  WHERE table_name=%s""", [table_name])
        self.cur.execute(sql)

        table_columns = self.cur.fetchall()[1:]

        column_names_to_add = list()
        for key in data:
            # if key does not exist
            if len([x[0] for x in table_columns if x[0] == key.lower()]) == 0:
                column_names_to_add.append(key.lower())

        for column_name in column_names_to_add:

            try:
                data_type = data_types[type(data[column_name])]
            except KeyError:
                data_type = data_types[str]

            sql = "ALTER TABLE %s ADD COLUMN %s %s;" % (table_name, column_name, data_type)

            try:
                self.cur.execute(sql)

            except psycopg2.DatabaseError as e:
                logger.error(e.pgcode + " " + e.pgerror.replace("\n", " "))

        self.con.commit()

        return len(column_names_to_add)

    def insert_values(self, table_name, data):
        # todo table_name = re.sub('[^A-Za-z0-9]+', '', table_name)
        # empty string list item that there will be a comma after column timestamp
        columns = [""]
        values = list()
        for key in data["data"]:
            columns.append(key)
            values.append(data["data"][key])

        params = [data["timestamp"]]
        params.extend(values)
        sql = self.cur.mogrify("INSERT INTO " + table_name + " (timestamp" + ', '.join(columns) + ")"
                               " VALUES (to_timestamp(%s)" + ', %s' * len(values) + ");", params)

        try:
            self.cur.execute(sql)
            self.con.commit()

        except psycopg2.OperationalError as e:
            logger.error(e)
            raise e

        except psycopg2.DatabaseError as e:

            if e.pgcode == "23505":
                logger.warn(e.pgcode + " " + e.pgerror.replace("\n", " "))
                self.con.commit()
            else:
                logger.error(e.pgcode + " " + e.pgerror.replace("\n", " "))
