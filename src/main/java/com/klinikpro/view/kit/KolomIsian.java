package com.klinikpro.view.kit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

/** Kolom isian teks dan password dengan sudut membulat dan teks petunjuk (placeholder). */
public final class KolomIsian {

    private KolomIsian() {
    }

    public static JTextField teks(String petunjuk) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                lukisLatar(this, g);
                super.paintComponent(g);
                lukisPetunjuk(this, g, petunjuk, getText().isEmpty());
                lukisGaris(this, g);
            }
        };
        siapkan(f);
        return f;
    }

    public static JPasswordField password(String petunjuk) {
        JPasswordField f = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                lukisLatar(this, g);
                super.paintComponent(g);
                lukisPetunjuk(this, g, petunjuk, getPassword().length == 0);
                lukisGaris(this, g);
            }
        };
        siapkan(f);
        return f;
    }

    private static void siapkan(JTextComponent f) {
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(0, 14, 0, 14));
        f.setFont(Tema.huruf(Font.PLAIN, 15));
        f.setForeground(Tema.TEKS);
        f.setCaretColor(Tema.UTAMA);
        f.setPreferredSize(new Dimension(240, 48));
        f.setMinimumSize(new Dimension(120, 48));
        f.putClientProperty("JComponent.outline", null);
        f.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { f.repaint(); }
            @Override
            public void focusLost(FocusEvent e) { f.repaint(); }
        });
    }

    private static void lukisLatar(JTextComponent c, Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.isEnabled() ? Color.WHITE : Tema.LATAR);
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
        g2.dispose();
    }

    private static void lukisGaris(JTextComponent c, Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.hasFocus() ? Tema.UTAMA : Tema.GARIS_INPUT);
        g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 12, 12);
        g2.dispose();
    }

    private static void lukisPetunjuk(JTextComponent c, Graphics g, String petunjuk, boolean kosong) {
        if (!kosong || petunjuk == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(new Color(0x7D8A88));
        g2.setFont(c.getFont());
        int y = (c.getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
        g2.drawString(petunjuk, c.getInsets().left, y);
        g2.dispose();
    }
}
