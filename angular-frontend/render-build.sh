#!/bin/bash
# Script de build para Render

echo "🔧 Instalando dependencias..."
npm install

echo "🏗️ Building Angular app..."
npm run build

echo "✅ Build completado!"
