/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.gui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import hourai.requesttester.RequestProcessor;
import hourai.requesttester.RequestServer;
import hourai.requesttester.data.RequestServerConnection;
import hourai.requesttester.implementation.UniqueNameGenerator;
import hourai.requesttester.implementation.factories.FileFinderFactory;
import hourai.requesttester.implementation.factories.FileReaderResponseGeneratorFactory;
import hourai.requesttester.implementation.factories.ProxyServerResponseGeneratorFactory;
import hourai.requesttester.implementation.factories.RequestFileWriterFactoryImpl;
import hourai.requesttester.interfaces.RequestWriteCallback;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;

/**
 *
 * @author nhansen
 */
public class ServerGui extends javax.swing.JFrame implements RequestWriteCallback {

    private RequestServer server;
    private boolean ignoreContent = false;
    private boolean recievedNewLine = false;
    private CountDownLatch awaitWindow = null;
	private boolean tolerantServer = true;

    private void setCountDown(CountDownLatch awaitWindow) {
        this.awaitWindow = awaitWindow;
    }

    private class ServerThread implements Runnable {

        private final RequestServer server;
        private final RequestWriteCallback callback;
        private final RequestProcessor processor;

        public ServerThread(RequestServer server, RequestWriteCallback callback, RequestProcessor processor) {
            this.server = server;
            this.processor = processor;
            this.callback = callback;
        }

        @Override
        public void run() {
            RequestServerConnection connection;
            while ((connection = server.getConnection()) != null) {
                //We want to attach our call back
                connection.getReader().addCallback(callback);
                //Figure out if the 
                processor.process(connection);
                ignoreContent = false;
                recievedNewLine = false;
            }
        }
    }

    /**
     * Creates new form ServerGui
     */
    public ServerGui() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        strategyCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        tolerantServerButton = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTextField1.setText("13286");
        jTextField1.setToolTipText("The port the service should run on. Connect to this application by using http://localhost:<port>\nOr http://<thisComputerIP>:<port>");

        jLabel1.setText("Listening Port");

        jToggleButton1.setText("Start");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Proxy Destination");

        jTextField2.setToolTipText("Populate this to forward messages to another server. Requests will still be written to the log and disk. Reponses will be as well. ");

        jLabel3.setText("Log");

        jButton1.setLabel("Clear Log");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        logTextArea.setEditable(false);
        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        logTextArea.setAutoscrolls(false);
        jScrollPane2.setViewportView(logTextArea);

        strategyCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ignore", "Underscore" }));
        strategyCombo.setToolTipText("The Strategy to use to find files to serve on disk");

        jLabel4.setText("Strategy");

        tolerantServerButton.setSelected(true);
        tolerantServerButton.setText("Tolerant Server");
        tolerantServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableDisableTolerantServer(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(strategyCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(tolerantServerButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1)
                    .addComponent(jToggleButton1)
                    .addComponent(jButton1)
                    .addComponent(strategyCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tolerantServerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if (jToggleButton1.isSelected()) {
            int port = Integer.parseInt(jTextField1.getText());
            logTextArea.append("\n");
            ResponseGeneratorFactory responseFactory;
            UniqueNameGenerator sharedGenerator = new UniqueNameGenerator();
            jTextField2.setEditable(false);
            jTextField1.setEditable(false);
            strategyCombo.setEditable(false);
			tolerantServerButton.setEnabled(false);
            if (!"".equals(jTextField2.getText())) {
                responseFactory = new ProxyServerResponseGeneratorFactory(jTextField2.getText(), sharedGenerator, this, tolerantServer);
            } else {
                FileFinderFactory.Method method = strategyCombo.getSelectedItem().equals("Ignore") ? FileFinderFactory.Method.Strip : FileFinderFactory.Method.Underscore;
                responseFactory = new FileReaderResponseGeneratorFactory(new FileFinderFactory(null, method));
            }
            server = new RequestServer(port, responseFactory);
            logTextArea.append("Server started on port ");
            logTextArea.append(server.getPort() + "\n");
            if (port != server.getPort()) {
                logTextArea.append("SERVER STARTED ON A DIFFERENT PORT\n");
            }
            Thread serverThread = new Thread(new ServerThread(server, this, new RequestProcessor(new RequestFileWriterFactoryImpl(sharedGenerator))));
            serverThread.setName("ServerThread-" + port);
            serverThread.start();
        } else {
            try {
                server.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTextField2.setEditable(true);
			strategyCombo.setEditable(true);
			tolerantServerButton.setEnabled(true);
            jTextField1.setEditable(true);
            logTextArea.append("Server stopped\n");
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        logTextArea.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (awaitWindow != null) {
            awaitWindow.countDown();
        }
    }//GEN-LAST:event_formWindowClosed

    private void enableDisableTolerantServer(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableDisableTolerantServer
		// TODO add your handling code here:
		tolerantServer = !tolerantServer;
    }//GEN-LAST:event_enableDisableTolerantServer

    public static void StartApplication(boolean await) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        final CountDownLatch awaitWindow = new CountDownLatch(1);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerGui gui = new ServerGui();
                gui.setCountDown(awaitWindow);
                gui.setVisible(true);
            }
        });
        if (await) {
            try {
                //Wait for the UI to terminate (keeping console window active for logging)
                awaitWindow.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JComboBox strategyCombo;
    private javax.swing.JToggleButton tolerantServerButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void recievedLine(final String line) {
        try {
            final String contentEncoding = "content-encoding: gzip";
            final String transferEncoding = "transfer-encoding: chunked";
            if (line.regionMatches(true, 0, contentEncoding, 0, contentEncoding.length())
                    || line.regionMatches(true, 0, transferEncoding, 0, transferEncoding.length())) {
                //Disable buffer until we restart
                ignoreContent = true;
                recievedNewLine = false;
            }
            if (ignoreContent && recievedNewLine) {
                return;
            }
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    //Threading misshap
                    if (ignoreContent && recievedNewLine) {
                        return;
                    }
                    if (ignoreContent && "".equals(line)) {
                        recievedNewLine = true;
                    }
                    logTextArea.append(line);
                    logTextArea.append("\n");
                    if (ignoreContent && recievedNewLine) {
                        logTextArea.append("Further Content is ignored for due to significant log creation");
                    }
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void recievedChar(final char c) {
        try {
            if (ignoreContent && recievedNewLine) {
                return;
            }
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    logTextArea.append(String.valueOf(c));
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ServerGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
