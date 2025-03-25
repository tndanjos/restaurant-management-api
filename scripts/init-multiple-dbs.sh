#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE restaurant_management_api_development;
    CREATE DATABASE restaurant_management_api_test;
EOSQL