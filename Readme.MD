
# Franchise Management API

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal/Clean-lightgrey)

Sitema robusto para la gestión centralizada de franquicias, sucursales y control de inventarios (productos). Esta API permite a las organizaciones administrar la jerarquía de sus negocios y consultar métricas clave como el stock máximo por sucursal.

---

##  Estructura del Proyecto

El código sigue los principios de **Arquitectura Hexagonal (Clean Architecture)**, dividiendo las responsabilidades en:
* **Entrypoints (Controllers)**: Manejo de peticiones HTTP y mapeo de DTOs.
* **Application (Ports/UseCases)**: Lógica de negocio e interfaces de entrada/salida.
* **Domain**: Entidades de negocio y reglas core (independientes del framework).
* **Infrastructure**: Implementaciones de persistencia y servicios externos.

---

## ️ Instalación y Despliegue

### Requisitos Previos
* Docker y Docker Compose instalados.
* Java 17+ (para ejecución local sin Docker).

### Pasos para Ejecutar
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/MarlonAnacona/franchise-management-api
    ```

2.  **Levantar infraestructura con Docker:**
    Este comando iniciará la base de datos y la aplicación automáticamente.
    ```bash
    docker-compose up -d
    ```
    En caso de que falle reintentar una vez mas, es posible esto por problemas de caché.


3.  **Acceso:**
    La API estará disponible en `http://localhost:8082/app/v1`.

---

##  Documentación de la API (Endpoints)

##  Pruebas con Postman

Se incluye el archivo `postman/franchise-management.postman_collection.json` en la raíz del proyecto para facilitar las pruebas de integración. además de los ambientes respectivos


1.  Abre Postman e importa la colección.
2.  Configura la variable `URL_BASE` (ej: `http://localhost:8080`).
3.  Utiliza los endpoints preconfigurados para validar el flujo completo.

La API fue desplegada en render y en el postman collection se dejan las respectivas colecciones para cada caso

` URL_BASE = localhost
`

` URL_AWS = URL del ALB en AWS`

` URL_RENDER = URL del servicio en render
`


### 1. Franquicias (`/v1/franchise`)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/` | Registra una nueva franquicia. |
| **PATCH** | `/{id}` | Actualiza el nombre de una franquicia existente. |
| **GET** | `/{id}/branches/top` | Retorna el producto con mayor stock por cada sucursal de una franquicia. |

### Crear Franquicia
`POST /v1/franchise`

#### Método: POST
**Cuerpo de la petición (Request):**

        {
            "name": "KFC"
        }
Respuesta (Response):

        {
        "name": "KFC",
        "franchiseId": 1
        }

### Actualizar Nombre de Franquicia
`PATCH /v1/franchise/{id}`
#### Método: PATCH
**Cuerpo de la petición (Request):**

        
        {
        "name": "Mc Donald´s"
        }


Respuesta (Response):

       {
        "name": "Mc Donald´s"
        }

### Obtener Producto con Mayor Stock por Sucursal

`GET /v1/franchise/{id}/branches/top`

#### Método: GET
Descripción: Retorna el producto con más existencias de cada sucursal perteneciente a una franquicia específica.

Respuesta (Response):

        [
        {
        "branchName": "bochalema",
        "productName": "papas",
        "stock": 60
        }
        ]

### 2. Sucursales (`/v1/branch`)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/` | Agrega una nueva sucursal a una franquicia. |
| **PATCH** | `/{id}` | Modifica el nombre de una sucursal. |
| **DELETE** | `/{branchId}/products/{productId}` | Elimina un producto de una sucursal específica. |

### Crear Sucursal
`POST /v1/branch`

#### Método: POST
**Cuerpo de la petición (Request):**

```json
{
    "name": "Sucursal Norte",
    "franchiseId": 1
}
```

Respuesta (Response):

```json
{
    "id": 10,
    "name": "Sucursal Norte",
    "franchiseId": 1
}
```

---

### Actualizar Nombre de Sucursal
`PATCH /v1/branch/{id}`

#### Método: PATCH
**Cuerpo de la petición (Request):**

```json
{
    "name": "Sucursal Norte Renovada"
}
```

Respuesta (Response):

```json
{
    "id": 10,
    "name": "Sucursal Norte Renovada"
}
```

---

### Eliminar Producto de una Sucursal
`DELETE /v1/branch/{branchId}/products/{productId}`

#### Método: DELETE

Descripción: Elimina un producto específico de una sucursal.

Respuesta (Response):

```text
No Content
```

---


### 3. Productos (`/v1/product`)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/` | Crea un nuevo producto y lo asigna a una sucursal. |
| **PATCH** | `/{id}/stock` | Actualiza la cantidad de stock de un producto. |
| **PATCH** | `/{id}/name` | Actualiza el nombre de un producto. |

---

### Crear Producto
`POST /v1/product`

#### Método: POST
**Cuerpo de la petición (Request):**

```json
{
    "name": "Hamburguesa",
    "stock": 50,
    "branchId": 10
}
```

Respuesta (Response):

```json
{
    "id": 100,
    "name": "Hamburguesa",
    "stock": 50,
    "branchId": 10
}
```

---

### Actualizar Stock de Producto
`PATCH /v1/product/{id}/stock`

#### Método: PATCH
**Cuerpo de la petición (Request):**

```json
{
    "stock": 75
}
```

Respuesta (Response):

```json
{
    "id": 100,
    "stock": 75
}
```

---

### Actualizar Nombre de Producto
`PATCH /v1/product/{idProduct}/name`

#### Método: PATCH
**Cuerpo de la petición (Request):**

```json
{
    "name": "Hamburguesa Especial"
}
```

Respuesta (Response):

```json
{
    "id": 100,
    "name": "Hamburguesa Especial"
}
```

---

##  Docker Compose

El archivo `docker-compose.yml` gestiona la orquestación de la aplicación y la base de datos.

Para ver los logs en tiempo real:



#  Deploy de Franchise Management API en AWS (ECS Fargate + ALB + RDS)

paso a paso cómo desplegar la aplicación en AWS usando **ECS Fargate**, **Application Load Balancer** y **RDS PostgreSQL**.

---

#  Arquitectura

* ECS Fargate → ejecuta el contenedor
* ALB → expone la API públicamente
* RDS PostgreSQL → base de datos
* ECR → repositorio de imágenes Docker
* CloudWatch → logs

---

#  Importante

Debido a que el servicio ECS depende de una imagen existente, el despliegue se realiza en **3 fases**:

```text
1. Crear infraestructura (sin ejecutar contenedor)
2. Subir imagen Docker a ECR
3. Desplegar y activar el servicio
```

---

#  Paso 1 — Crear la infraestructura (sin contenedores)

El servicio se crea con `DesiredCount = 0` para evitar fallos por health check.

```bash
aws cloudformation create-stack \
--stack-name franchise-stack \
--template-body file://cloudformation.yaml \
--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
--parameters ParameterKey=ContainerImage,ParameterValue=dummy \
--region <REGION>
```

Esperar a que finalice:

```bash
aws cloudformation wait stack-create-complete \
--stack-name franchise-stack \
--region <REGION>
```

---

#  Paso 2 — Subir la imagen a ECR

##  Login a ECR

```bash
aws ecr get-login-password --region <REGION> | docker login \
--username AWS \
--password-stdin <TU-ID-AWS>.dkr.ecr.<REGION>.amazonaws.com
```

---

##  Build de la imagen

```bash
docker build -t franchise-management-api .
```

---

##  Tag

```bash
docker tag franchise-management-api:latest \
<TU-ID-AWS>.dkr.ecr.<REGION>.amazonaws.com/franchise-management-api:latest
```

---

##  Push

```bash
docker push \
<TU-ID-AWS>.dkr.ecr.<REGION>.amazonaws.com/franchise-management-api:latest
```

---

#  Paso 3 — Actualizar el stack con la imagen real

```bash
aws cloudformation update-stack \
--stack-name franchise-stack \
--template-body file://cloudformation.yaml \
--capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
--parameters ParameterKey=ContainerImage,ParameterValue=<TU-ID-AWS>.dkr.ecr.<REGION>.amazonaws.com/franchise-management-api:latest \
--region <REGION>
```

Esperar:

```bash
aws cloudformation wait stack-update-complete \
--stack-name franchise-stack \
--region <REGION>
```

---

#  Paso 4 — Activar el servicio ECS

```bash
aws ecs update-service \
--cluster franchise-stack \
--service franchise-stack \
--desired-count 1 \
--force-new-deployment \
--region <REGION>
```

---

#  Paso 5 — Obtener URL pública

```bash
aws cloudformation describe-stacks \
--stack-name franchise-stack \
--query "Stacks[0].Outputs[?OutputKey=='ALBDNS'].OutputValue" \
--region <REGION>
```

Ejemplo:

```text
http://xxxxx.elb.amazonaws.com
```

---

#  Paso 6 — Probar la API

```bash
curl http://<ALB-DNS>/app/actuator/health
```

Respuesta esperada:

```json
{"status":"UP"}
```

---

#  Reglas clave

```text
ALB → puerto 80
Spring Boot → puerto 8080
HealthCheck → incluir context-path (/app)
```

---

#  Flujo final

```text
1. Crear stack (DesiredCount=0)
2. Subir imagen a ECR
3. Actualizar stack
4. Activar ECS (DesiredCount=1)
5. Probar endpoint
```

---

#  Resultado

La API queda disponible públicamente a través del Application Load Balancer.


