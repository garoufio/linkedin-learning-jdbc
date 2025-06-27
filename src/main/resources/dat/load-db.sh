#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

export PGDATABASE=$POSTGRES_DB
export PGHOST=localhost
export PGUSER=$POSTGRES_USER
export PGPASSWORD=$POSTGRES_PASSWORD

# load database
#psql -a -f ./data.sql