# 🌱 Sistema de Gerenciamento de Horta

Um sistema completo para gerenciamento de hortas desenvolvido com Spring Boot, oferecendo uma API REST robusta para controle de plantas, cuidados e alertas automáticos.

## 📋 Funcionalidades

### 🌿 Gerenciamento de Plantas
- ✅ Cadastro, edição e remoção de plantas
- ✅ Busca por nome, tipo e região
- ✅ Cálculo automático de datas de colheita
- ✅ Sugestões de plantas por região
- ✅ Controle de ciclo de vida das plantas

### 🛠️ Controle de Cuidados
- ✅ Registro de rega, poda, colheita e outros cuidados
- ✅ Histórico completo de cuidados por planta
- ✅ Estatísticas de cuidados
- ✅ Validação de dados e regras de negócio

### 🔔 Alertas Automáticos
- ✅ Notificações diárias para rega (8h)
- ✅ Alertas semanais para poda (segundas 9h)
- ✅ Verificação de plantas prontas para colheita (18h)
- ✅ Relatórios semanais automáticos (domingos 20h)

### 📊 Relatórios e Estatísticas
- ✅ Estatísticas por tipo de planta
- ✅ Estatísticas por tipo de cuidado
- ✅ Plantas recentes e histórico
- ✅ Relatórios automáticos

## 🏗️ Arquitetura

### Tecnologias Utilizadas
- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados principal
- **H2 Database** - Banco para desenvolvimento/testes
- **Spring Scheduler** - Agendamento de tarefas
- **Swagger/OpenAPI** - Documentação da API
- **JUnit 5 + Mockito** - Testes unitários
- **Thymeleaf** - Templates web (opcional)

### Estrutura do Projeto
```
src/
├── main/
│   ├── java/com/horta/
│   │   ├── controller/     # Controllers REST
│   │   ├── service/        # Lógica de negócio
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositórios de dados
│   │   ├── dto/            # Data Transfer Objects
│   │   └── config/         # Configurações
│   └── resources/
│       ├── application.properties
│       └── templates/      # Templates Thymeleaf
└── test/                   # Testes unitários e integração
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior
- PostgreSQL 12 ou superior (opcional, pode usar H2)

### Configuração do Banco de Dados

#### Opção 1: PostgreSQL (Produção)
1. Instale o PostgreSQL
2. Crie um banco de dados chamado `sistema_horta`
3. Configure as credenciais no `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_horta
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

#### Opção 2: H2 (Desenvolvimento)
Descomente as linhas do H2 no `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### Executando a Aplicação

1. **Clone o projeto**
```bash
git clone <url-do-repositorio>
cd sistema-horta
```

2. **Compile e execute**
```bash
mvn clean install
mvn spring-boot:run
```

3. **Acesse a aplicação**
- API: http://localhost:8080/api
- Documentação Swagger: http://localhost:8080/api/swagger-ui.html
- Console H2 (se habilitado): http://localhost:8080/api/h2-console

## 📖 Documentação da API

### Endpoints Principais

#### 🌱 Plantas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/plantas` | Lista todas as plantas |
| POST | `/plantas` | Cadastra nova planta |
| GET | `/plantas/{id}` | Busca planta por ID |
| PUT | `/plantas/{id}` | Atualiza planta |
| DELETE | `/plantas/{id}` | Remove planta |
| GET | `/plantas/buscar/nome?nome=X` | Busca por nome |
| GET | `/plantas/buscar/tipo?tipo=X` | Busca por tipo |
| GET | `/plantas/buscar/regiao?regiao=X` | Busca por região |
| GET | `/plantas/colheita` | Plantas prontas para colheita |
| GET | `/plantas/rega` | Plantas que precisam de rega |
| GET | `/plantas/poda` | Plantas que precisam de poda |
| GET | `/plantas/sugestoes?regiao=X` | Sugestões por região |

#### 🛠️ Cuidados
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/cuidados` | Lista todos os cuidados |
| POST | `/cuidados` | Registra novo cuidado |
| GET | `/cuidados/{id}` | Busca cuidado por ID |
| PUT | `/cuidados/{id}` | Atualiza cuidado |
| DELETE | `/cuidados/{id}` | Remove cuidado |
| GET | `/cuidados/planta/{plantaId}` | Histórico da planta |
| POST | `/cuidados/rega/{plantaId}` | Registra rega |
| POST | `/cuidados/poda/{plantaId}` | Registra poda |
| POST | `/cuidados/colheita/{plantaId}` | Registra colheita |

#### 🔔 Alertas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/alertas/status` | Status do sistema de alertas |
| POST | `/alertas/testar` | Testa todos os alertas |
| POST | `/alertas/rega` | Verifica plantas para rega |
| POST | `/alertas/poda` | Verifica plantas para poda |
| POST | `/alertas/colheita` | Verifica plantas para colheita |

### Exemplos de Uso

#### Cadastrar uma Planta
```bash
curl -X POST http://localhost:8080/api/plantas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Tomate Cereja",
    "tipo": "Hortaliça",
    "dataPlantio": "2024-01-15",
    "cicloDias": 90,
    "regiao": "Sul",
    "descricao": "Tomate cereja orgânico",
    "diasEntreRegas": 3,
    "diasEntrePodas": 30
  }'
```

#### Registrar uma Rega
```bash
curl -X POST "http://localhost:8080/api/cuidados/rega/1?observacoes=Rega%20normal&usuarioResponsavel=João"
```

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
mvn test

# Apenas testes unitários
mvn test -Dtest="*Test"

# Apenas testes de integração
mvn test -Dtest="*IT"
```

### Cobertura de Testes
- ✅ Testes unitários para Services
- ✅ Testes de integração para Controllers
- ✅ Mocks com Mockito
- ✅ Validação de regras de negócio

## 📊 Monitoramento

### Logs
O sistema gera logs detalhados para:
- Operações CRUD
- Execução de alertas automáticos
- Erros e exceções
- Estatísticas de uso

### Alertas Automáticos
- **Rega**: Diariamente às 8h
- **Poda**: Segundas-feiras às 9h  
- **Colheita**: Diariamente às 18h
- **Relatório**: Domingos às 20h

## 🔧 Configurações Avançadas

### Personalizar Horários dos Alertas
Edite as anotações `@Scheduled` no `AlertaService.java`:
```java
@Scheduled(cron = "0 0 8 * * *") // Formato: segundo minuto hora dia mês dia-da-semana
```

### Configurar Email/SMS
Implemente os métodos de notificação no `AlertaService.java`:
```java
private void enviarNotificacaoRega(List<PlantaDTO> plantas) {
    // Implementar envio de email/SMS
}
```

### Adicionar Novos Tipos de Cuidado
Edite o enum `TipoCuidado` em `Cuidado.java`:
```java
public enum TipoCuidado {
    REGA("Rega"),
    PODA("Poda"),
    COLHEITA("Colheita"),
    FERTILIZACAO("Fertilização"),
    // Adicione novos tipos aqui
}
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

- 📧 Email: mkarvat10@gmail.com


