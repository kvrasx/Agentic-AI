[![progress-banner](https://backend.codecrafters.io/progress/claude-code/b8415420-9743-4b82-aa51-9c92bcc825b5)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

# 🧠 Claude Code (Java) — LLM-Powered Coding Agent

A lightweight, from-scratch implementation of an **agentic AI coding assistant**, inspired by Claude Code.

This project demonstrates how to build a tool-using AI agent using **OpenAI-compatible function calling**, a custom **agent loop**, and modular **tool execution** — all in Java.

---

## ✨ Features

* 🔧 **Tool Calling Engine**

  * OpenAI-compatible function/tool calling
  * Dynamic tool selection by the LLM

* 🔁 **Agent Loop (Core Runtime)**

  * Iterative reasoning loop (LLM → tool → result → LLM)
  * Handles multi-step tasks autonomously

* 🧠 **State Management**

  * Conversation history tracking
  * Context-aware responses

* 🛠️ **Pluggable Tools**

  * Easily extend with new capabilities (filesystem, shell, APIs, etc.)

* 🌐 **HTTP Integration**

  * Communicates with LLM APIs via REST

---

## 🏗️ Architecture

This project implements a minimal **agent runtime**, composed of:

```text
User Input
   ↓
LLM (Planner)
   ↓
Tool Call Decision
   ↓
Tool Executor
   ↓
Result → back to LLM
   ↓
Final Response
```

### Core Components

* **Agent Loop**
  Controls execution flow and iterative reasoning

* **Tool Executor**
  Maps tool calls to actual Java functions

* **Message State**
  Maintains conversation history for context

* **LLM Client**
  Handles API communication with the model

---

## 🔁 Agent Loop (Simplified)

```java
while (true) {
    Response response = llm(messages);

    if (response.hasToolCall()) {
        ToolResult result = executeTool(response.getToolCall());
        messages.add(result);
    } else {
        return response;
    }
}
```

This loop is the **core primitive behind agentic AI systems**.

---

## 🚀 Getting Started

### Prerequisites

* Java 23
* Maven

### Run locally

```bash
./your_program.sh
```

### Submit (Codecrafters)

```bash
codecrafters submit
```

---

## 📦 Project Structure

```text
src/main/java/
 ├── Main.java          # Entry point
 ├── Agent Loop         # Core reasoning loop
 ├── Tool Handlers      # Tool implementations
 └── LLM Client         # API integration
```

---

## 🧩 Example Use Cases

* Code explanation
* File manipulation via tools
* Multi-step reasoning tasks
* Building autonomous dev assistants

---

## 📚 What I Learned

This project deepened my understanding of:

* Agentic AI system design
* Tool calling & function schemas
* State management in LLM workflows
* Designing execution loops for autonomous agents

---

## 🔮 Future Improvements

* Persistent memory (database / vector store)
* Better planning (multi-step task decomposition)
* Tool reliability (retries, error handling)
* Streaming responses
* Multi-agent collaboration

