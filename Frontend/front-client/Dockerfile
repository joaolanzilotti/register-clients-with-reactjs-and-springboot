# Usar uma imagem base Node.js
FROM node:14-alpine

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo package.json para o diretório de trabalho
COPY package*.json ./

# Instalar as dependências
RUN npm install

# Copiar os arquivos da aplicação para o diretório de trabalho
COPY . .

# Compilar a aplicação React
RUN npm run build

# Configurar a porta em que a aplicação irá rodar dentro do contêiner
ENV PORT=3000

# Expor a porta da aplicação
EXPOSE $PORT

# Comando para iniciar a aplicação
CMD [ "npm", "start" ]
