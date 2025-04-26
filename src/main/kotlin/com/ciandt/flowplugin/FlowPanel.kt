// FlowPanel.kt - Interface principal da IA Flow

package com.ciandt.flowplugin

import com.intellij.openapi.project.Project
import java.awt.*
import java.awt.event.ActionEvent
import javax.swing.*

class FlowPanel(private val project: Project) {
    val mainPanel: JPanel = JPanel(BorderLayout())

    private val headerPanel = JPanel(BorderLayout())
    private val chatListPanel = JPanel()
    private val chatPanel = JPanel(BorderLayout())
    private val inputPanel = JPanel(BorderLayout())
    private val optionsPanel = JPanel(FlowLayout(FlowLayout.RIGHT))

    private val chatScrollPane: JScrollPane
    private val inputTextArea = JTextArea(2, 50)
    private val sendButton = JButton("→")
    private val loadingLabel = JLabel("...")
    private val languageSelector = JComboBox(arrayOf("Português", "English", "Español"))

    init {
        // Estilo base
        mainPanel.background = Color(36, 36, 36)
        chatListPanel.layout = BoxLayout(chatListPanel, BoxLayout.Y_AXIS)
        chatListPanel.background = Color(45, 45, 45)
        chatScrollPane = JScrollPane(chatListPanel)
        chatScrollPane.border = null

        // Cabeçalho com opções e linguagem
        headerPanel.background = Color(28, 28, 28)
        headerPanel.add(languageSelector, BorderLayout.WEST)

        val newChatButton = JButton("+ Novo chat")
        newChatButton.addActionListener { addSystemMessage("[Novo chat iniciado]") }

        val minimizeButton = JButton("_ Minimizar")
        minimizeButton.addActionListener { mainPanel.isVisible = false }

        val configButton = JButton("⚙ Configurações")
        optionsPanel.add(newChatButton)
        optionsPanel.add(minimizeButton)
        optionsPanel.add(configButton)

        headerPanel.add(optionsPanel, BorderLayout.EAST)

        // Campo de entrada
        inputTextArea.lineWrap = true
        inputTextArea.wrapStyleWord = true
        inputPanel.add(inputTextArea, BorderLayout.CENTER)
        sendButton.addActionListener(this::handleSend)
        inputPanel.add(sendButton, BorderLayout.EAST)
        inputPanel.background = Color(30, 30, 30)

        // Área de conversa
        chatPanel.add(chatScrollPane, BorderLayout.CENTER)
        chatPanel.add(inputPanel, BorderLayout.SOUTH)
        chatPanel.background = Color(36, 36, 36)

        // Montagem final
        mainPanel.add(headerPanel, BorderLayout.NORTH)
        mainPanel.add(chatPanel, BorderLayout.CENTER)
    }

    private fun handleSend(e: ActionEvent) {
        val text = inputTextArea.text.trim()
        if (text.isNotEmpty()) {
            addUserMessage(text)
            inputTextArea.text = ""
            sendButton.isEnabled = false
            showLoading(true)

            // Simula processamento
            SwingUtilities.invokeLater {
                Thread.sleep(1200)
                addBotMessage("Resposta gerada para: '$text'")
                sendButton.isEnabled = true
                showLoading(false)
            }
        }
    }

    private fun addUserMessage(text: String) {
        val label = JLabel("Você: $text")
        label.foreground = Color.WHITE
        chatListPanel.add(label)
        chatListPanel.revalidate()
    }

    private fun addBotMessage(text: String) {
        val label = JLabel("Flow: $text")
        label.foreground = Color(130, 200, 255)
        chatListPanel.add(label)
        chatListPanel.revalidate()
    }

    private fun addSystemMessage(text: String) {
        val label = JLabel(text)
        label.foreground = Color.GRAY
        chatListPanel.add(label)
        chatListPanel.revalidate()
    }

    private fun showLoading(visible: Boolean) {
        if (visible) {
            loadingLabel.text = "..."
            inputPanel.add(loadingLabel, BorderLayout.WEST)
        } else {
            inputPanel.remove(loadingLabel)
        }
        inputPanel.revalidate()
        inputPanel.repaint()
    }
}
