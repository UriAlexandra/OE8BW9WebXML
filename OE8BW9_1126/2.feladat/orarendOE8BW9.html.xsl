<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="/">
    <html>
      <head>
        <meta charset="UTF-8"/>
        <title>Uri Alexandra Nikoletta Órarend - 2025. I. félév.</title>
      </head>

      <body>
        <h2>Uri Alexandra Nikoletta Órarend - 2025. I. félév.</h2>

        <table border="1" cellpadding="6">
          <tr>
            <th>ID</th>
            <th>Típus</th>
            <th>Tantárgy</th>
            <th>Nap</th>
            <th>Tól</th>
            <th>Ig</th>
            <th>Helyszín</th>
            <th>Oktató</th>
            <th>Szak</th>
          </tr>

          <xsl:for-each select="UAN_orarend/ora">
            <tr>
              <td><xsl:value-of select="@id"/></td>
              <td><xsl:value-of select="@tipus"/></td>
              <td><xsl:value-of select="tantargy"/></td>
              <td><xsl:value-of select="idopont/nap"/></td>
              <td><xsl:value-of select="idopont/tol"/></td>
              <td><xsl:value-of select="idopont/ig"/></td>
              <td><xsl:value-of select="helyszín"/></td>
              <td><xsl:value-of select="oktato"/></td>
              <td><xsl:value-of select="szak"/></td>
            </tr>
          </xsl:for-each>
        </table>

      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
