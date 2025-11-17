#!/usr/bin/env bash
set -euo pipefail

#############################################
# Basic docker config
#############################################

IMAGE="apache/kafka:4.1.1"
CONTAINER_NAME="local-kafka"
HOST_PORT=9092
CONTAINER_PORT=9092

# Path to the topics script of the container
KAFKA_TOPICS_BIN="/opt/kafka/bin/kafka-topics.sh"

#############################################
# Topic list
#############################################
TOPICS=(
  "wikimedia-stream"
)

#############################################
# Run kafka container
#############################################

echo ">> Checking if container '${CONTAINER_NAME}' already exists..." 
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}\$"; then
  echo "   The container '${CONTAINER_NAME}' already exists..."
  docker start "${CONTAINER_NAME}" >/dev/null
else
  echo ">> Running kafka container:: '${CONTAINER_NAME}'..."
  docker run -d \
    --name "${CONTAINER_NAME}" \
    -p "${HOST_PORT}:${CONTAINER_PORT}" \
    "${IMAGE}" >/dev/null
fi

#############################################
# Wait until available
#############################################

echo ">> Waiting for kafka to run on localhost:${HOST_PORT}..."

MAX_RETRIES=30
SLEEP_SECONDS=2
KAFKA_READY=false

for i in $(seq 1 "${MAX_RETRIES}"); do
  if docker exec "${CONTAINER_NAME}" "${KAFKA_TOPICS_BIN}" \
      --bootstrap-server "localhost:${CONTAINER_PORT}" \
      --list >/dev/null 2>&1; then
    echo "   Kafka is ready (attempt n ${i})."
    KAFKA_READY=true
    break
  else
    echo "   Kafka is not ready (attempt ${i}/${MAX_RETRIES})..."
    sleep "${SLEEP_SECONDS}"
  fi
done

if [[ "${KAFKA_READY}" != true ]]; then
  echo "ERROR: Kafka hasnt boot on time. Check container logs"
  docker logs --tail 200 "${CONTAINER_NAME}" || true
  exit 1
fi

#############################################
# Conexion args
#############################################

CONNECTION_ARGS=(--bootstrap-server "localhost:${CONTAINER_PORT}")

#############################################
# Create topics
#############################################

for topic in "${TOPICS[@]}"; do
  echo ">> Creating topic '${topic}'..."
  docker exec "${CONTAINER_NAME}" "${KAFKA_TOPICS_BIN}" \
    --create \
    --topic "${topic}" \
    "${CONNECTION_ARGS[@]}" 
done

echo ">> All topics created. End of script"
for topic in "${TOPICS[@]}"; do
  echo "   - ${topic}"
done
