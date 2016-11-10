FROM smueller18/python3-kafka:alpine

MAINTAINER smueller18

# check what is temporarily needed
RUN apk add --virtual .build-deps \
          gcc \
          python3-dev \
          musl-dev \
          postgresql-dev
    && pip install psycopg2 \
    && apk del .build-deps

COPY src /consumer/postgres

WORKDIR /consumer/postgres

CMD ["python", "database_writer.py"]
