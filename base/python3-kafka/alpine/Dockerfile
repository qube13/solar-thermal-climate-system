FROM smueller18/base:python3-alpine

MAINTAINER smueller18

RUN apk add --no-cache libressl2.4-libcrypto@edge libressl2.4-libssl@edge librdkafka@testing librdkafka-dev@testing
RUN apk add --no-cache gcc python3-dev musl curl

RUN pip install --upgrade pip && \
    pip install avro_python3 && \
    pip install pykafka
    # apk del .build-deps

# Better mount it as volume in the containers needed
# so it is not baked into the image since this could change
# As of now, see suggestion /avro/schema/kafka.avsc
COPY kafka.avsc /opt/config/kafka.avsc
