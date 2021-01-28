## Microservizio ETL_L3

Il modulo ETL_L3, realizzato in linguaggio Java, si occupa di caricare i dati da topic Kafka, per riscriverli, dopo eventuali elaborazioni, su un altro sistema, nel caso in oggetto, in-memory data store Redis.

Non è stata data una specifica per la struttura dati da utilizzare, pertanto sono state operate scelte orientate dall'idea di mostrare i dati su una dashboard web. 

Per tale scopo, il microservizio utilizza la Consumer API di Kafka e l'API Jedis per la comunicazione con Redis.

### Configurazione
E' possibile configurare l'ETL_L3 mediante un file di properties, passato contestualmente al lancio del servizio, 
mediante l'opzione java `-Dproperties.file="ETL_L3.properties"`, contenente i seguenti campi:

- **Kafka.servers**: elenco di coppie `host:port` separate da virgola ',', usato nel caso in cui `OuputAdapter` sia `KafkaOutputChannelAdapter`
- **Redis.server**: coppia `host:port` per la connessione al server Redis
- **input.topic**: nome del topic Kafka da cui leggere i messaggi

## Funzionamento
La classe `ETL_L3` viene istanziata senza richiedere alcun parametro al costruttore, il quale crea un'istanza di 
`ConsumeToRedis`, passando una istanza astratta di `IWriter`. Nel caso in oggetto, l'unica implementazione è 
`RedisWriter`, che implementa l'interfaccia per la scrittura dei documenti su Redis. 

Tramite il metodo `execute`, il consumer si occupa di consumare i messaggi sul topic batch di Kafka, riscrivendoli su 
Redis secondo le modalità previste, definite tramite la classe `IWriter`. 


### RedisWriter
Questa è la classe che si occupa di effettuare la scrittura su Redis.

Essa si occupa principalmente di effettuare due operazioni sul db:

- `SADD vehicles <VIN_VEHICLE>`: Inserire (se non esiste già) il veicolo del messaggio in un _set_ con l'elenco dei 
veicoli esistenti,
- `ZADD readings/<VIN_VEHICLE> <TIMESTAMP> <DOCUMENT>`: Inserire il documento del messaggio in un _sorted set_ per il 
veicolo del messaggio, ordinato secondo il timestamp del messaggio. 
