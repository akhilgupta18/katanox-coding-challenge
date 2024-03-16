init-testcontainers:
	docker pull postgres:16.2
	docker pull testcontainers/ryuk:0.3.3

build-jar: init-testcontainers
	mvn clean package

build-docker: build-jar
	docker build . -t katanox-api-impl:1.0

build-run-docker: build-docker
	docker-compose -f docker-compose.yml up  --remove-orphans
