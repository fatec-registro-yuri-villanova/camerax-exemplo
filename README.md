# CameraX Exemplo - Jetpack Compose

## 📱 Sobre o Projeto

Este é um aplicativo Android desenvolvido com **Jetpack Compose** que demonstra o uso da biblioteca **CameraX** para captura de imagens. O projeto foi desenvolvido como material educacional para a **FATEC Registro** e serve como exemplo prático de implementação de câmera em aplicações Android modernas.

## ✨ Funcionalidades

- 📷 **Captura de Fotos**: Interface intuitiva para tirar fotos usando a câmera do dispositivo
- 👁️ **Pré-visualização em Tempo Real**: Exibição do preview da câmera antes da captura
- 🖼️ **Visualização de Imagem**: Preview da foto capturada antes de salvar
- 💾 **Salvar na Galeria**: Salva as fotos capturadas na galeria do dispositivo (pasta DCIM/CameraJetpack)
- 🗑️ **Descartar Foto**: Opção para descartar a foto e tirar uma nova
- 🔐 **Gerenciamento de Permissões**: Solicitação automática de permissão de câmera

## 🛠️ Tecnologias Utilizadas

### Linguagem e Framework
- **Kotlin** (2.0.21) - Linguagem de programação principal
- **Jetpack Compose** - Framework moderno de UI declarativa
- **Material Design 3** - Componentes de interface seguindo as diretrizes do Material Design

### Bibliotecas Principais
- **CameraX** (1.3.1) - API moderna de câmera do Android
  - `camera-core` - Funcionalidades principais
  - `camera-camera2` - Implementação Camera2
  - `camera-lifecycle` - Integração com lifecycle
  - `camera-view` - Componentes de visualização
- **Coil** (2.7.0) - Carregamento e exibição de imagens
- **AndroidX Core KTX** (1.17.0) - Extensões Kotlin para Android

### Ferramentas de Build
- **Gradle** (8.13.0-alpha04)
- **Android Gradle Plugin**
- **Kotlin Compose Compiler Plugin**

## 📋 Requisitos

- **Android Studio** Hedgehog (2023.1.1) ou superior
- **JDK** 11 ou superior
- **Android SDK**:
  - `minSdk`: 24 (Android 7.0 Nougat)
  - `targetSdk`: 36
  - `compileSdk`: 36
- Dispositivo Android ou emulador com câmera

## 🚀 Como Executar

### 1. Clone o Repositório
```bash
git clone https://github.com/fatec-registro-yuri-villanova/camerax-exemplo.git
cd camerax-exemplo
```

### 2. Abra no Android Studio
- Abra o Android Studio
- Selecione "Open an Existing Project"
- Navegue até a pasta do projeto e clique em "OK"
- Aguarde o Gradle sincronizar as dependências

### 3. Configure o Dispositivo
- Conecte um dispositivo Android via USB com depuração USB ativada, **OU**
- Configure um emulador Android (recomendado: Pixel 5 com API 33+)
- **Importante**: Certifique-se de que o dispositivo/emulador possui câmera disponível

### 4. Execute o Aplicativo
- Clique no botão "Run" (▶️) no Android Studio
- Selecione o dispositivo/emulador desejado
- O aplicativo será instalado e executado automaticamente

### 5. Permissões
- Na primeira execução, o app solicitará permissão para usar a câmera
- Conceda a permissão para utilizar o aplicativo

## 📐 Arquitetura do Projeto

```
app/src/main/java/com/fatec/camerajetpack/
├── MainActivity.kt                          # Activity principal
├── ui/
│   ├── screens/
│   │   └── MainScreen.kt                   # Tela principal com lógica de fluxo
│   ├── camera/
│   │   ├── CameraView.kt                   # Classe de controle da câmera
│   │   ├── PreviewView.kt                  # Composable do preview da câmera
│   │   └── ImagePreviewScreen.kt           # Tela de visualização da foto
│   └── theme/
│       ├── Color.kt                        # Definição de cores
│       ├── Theme.kt                        # Tema do aplicativo
│       └── Type.kt                         # Tipografia
└── utils/
    ├── PermissionUtils.kt                  # Gerenciamento de permissões
    └── ImageUtils.kt                       # Utilitários para salvar imagens
```

## 🏗️ Componentes Principais

### MainScreen
O coração do aplicativo que gerencia dois estados principais:
- **Modo Câmera**: Exibe o preview da câmera e o botão para capturar
- **Modo Preview**: Exibe a foto capturada com opções para salvar ou descartar

### CameraView (Classe de Controle)
Responsável por:
- Configurar o `ImageCapture` do CameraX
- Executar a captura de foto quando solicitado
- Gerenciar callbacks de sucesso e erro

### PreviewView (Composable)
Componente Compose que:
- Hospeda a `PreviewView` nativa do CameraX usando `AndroidView`
- Configura o preview da câmera em tempo real
- Vincula o lifecycle da câmera ao lifecycle do Compose

### ImagePreviewScreen
Tela de decisão que:
- Exibe a foto capturada usando Coil
- Oferece opções para salvar na galeria ou descartar

### PermissionUtils
Utilitário singleton para:
- Verificar se a permissão de câmera foi concedida
- Facilitar o gerenciamento de permissões

### ImageUtils
Utilitário singleton para:
- Salvar imagens no MediaStore (galeria do Android)
- Organizar fotos na pasta `DCIM/CameraJetpack`
- Limpar arquivos temporários após o salvamento

## 🎯 Fluxo de Uso

1. **Inicialização**: O app solicita permissão de câmera
2. **Preview**: Após concessão, exibe o preview da câmera
3. **Captura**: Usuário clica no botão "Tirar Foto"
4. **Visualização**: Foto capturada é exibida em tela cheia
5. **Decisão**: Usuário pode:
   - **Salvar**: Foto é salva na galeria e volta ao modo câmera
   - **Descartar**: Foto é descartada e volta ao modo câmera

## 🔒 Permissões Necessárias

O aplicativo requer as seguintes permissões declaradas no `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera.any" />
```

## 🎨 Interface do Usuário

O aplicativo utiliza:
- **Material Design 3** para componentes visuais
- **Compose** para UI declarativa e reativa
- **Edge-to-Edge** display para melhor aproveitamento da tela
- Botões flutuantes na parte inferior para ações principais

## 📝 Notas Técnicas

### Gerenciamento de Threads
- Utiliza `ExecutorService` com thread única para operações de I/O da câmera
- Garante que operações pesadas não bloqueiem a UI thread

### Armazenamento
- Fotos são temporariamente salvas no cache do app
- Após confirmação, são movidas para a galeria (MediaStore)
- Arquivos temporários são automaticamente deletados após o salvamento

### Compatibilidade
- Suporta Android 7.0 (API 24) ou superior
- Utiliza APIs modernas com fallback quando necessário
- Compatível com diferentes tamanhos de tela

## 🐛 Troubleshooting

### Problema: Câmera não inicializa
**Solução**: Verifique se as permissões foram concedidas e se o dispositivo possui câmera funcional

### Problema: Erro ao salvar foto
**Solução**: Verifique o espaço disponível no armazenamento do dispositivo

### Problema: Preview aparece distorcido
**Solução**: O app usa `ScaleType.FILL_CENTER` que pode cortar a imagem. Isso é intencional para melhor experiência visual

## 🤝 Contribuindo

Este é um projeto educacional. Sugestões e melhorias são bem-vindas!

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é de código aberto e está disponível para fins educacionais.

## 👨‍🏫 Autor

**FATEC Registro - Yuri Villanova**

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [CameraX Documentation](https://developer.android.com/training/camerax)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)

### Tutoriais Recomendados
- [CameraX Codelab](https://developer.android.com/codelabs/camerax-getting-started)
- [Compose Basics](https://developer.android.com/courses/pathways/compose)

---

**Desenvolvido com ❤️ para aprendizado de desenvolvimento Android moderno**
