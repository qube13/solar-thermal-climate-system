FROM smueller18/python3-kafka:alpine

MAINTAINER smueller18

RUN apk add --virtual .build-deps \
          gcc \
          python3-dev \
          musl-dev \
    && pip install flask \
    && apk del .build-deps

COPY src /consumer/monitor

WORKDIR /consumer/monitor

CMD ["python", "app.py"]
