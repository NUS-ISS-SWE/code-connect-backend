#!/bin/bash

# List of service directories and Docker image names (key=value format)
declare -A services=(
  ["codeconnect-liquibase-service"]="codeconnect-liquibase-service"
  ["codeconnect-admin-management-service"]="codeconnect-admin-management-service"
  ["codeconnect-api-gateway"]="codeconnect-api-gateway"
  ["codeconnect-interview-prep-service"]="codeconnect-interview-prep-service"
  ["codeconnect-job-management-service"]="codeconnect-job-management-service"
  ["codeconnect-user-management-service"]="codeconnect-user-management-service"
)

# Docker tag version
version="v1"

# DockerHub username
username="961125mingwei"

# Login once before the loop
echo "Logging in to DockerHub..."
docker login || { echo "Docker login failed"; exit 1; }

# Loop through each service
for dir in "${!services[@]}"; do
  image="${services[$dir]}"
  echo "======================================"
  echo "Processing $dir..."

  # Navigate to service directory
  cd "$dir" || { echo "Directory $dir not found"; exit 1; }

  # Build with Maven
  echo "Running 'mvn clean package' in $dir..."
  mvn clean package -DskipTests || { echo "Maven build failed for $dir"; exit 1; }

  # Build Docker image
  echo "Building Docker image $image:$version..."
  docker build -t "$image:$version" -f Dockerfile . || { echo "Docker build failed for $image"; exit 1; }

  # Tag and push
  docker tag "$image:$version" "$username/$image:$version"
  docker push "$username/$image:$version" || { echo "Push failed for $image"; exit 1; }

  echo "$image pushed successfully!"

  # Return to base directory
  cd - > /dev/null
done

echo "✅ All services built and deployed successfully!"
