#!/bin/sh
# Usage: ./wait-for.sh host:port -- command...

host="$1"
shift
cmd="$@"

until nc -z $(echo $host | cut -d: -f1) $(echo $host | cut -d: -f2); do
  echo "⏳ Waiting for $host to be available..."
  sleep 2
done

echo "✅ $host is up. Running: $cmd"
exec $cmd
