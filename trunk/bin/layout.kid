<?xml version='1.0' encoding='utf-8'?>
<?python

def empty():
    return ""

page_css = empty()
page_mate = empty()

?>
<html xmlns:py='http://purl.org/kid/ns#'>

  <head>
    <title/>
    ${page_css()}
    ${page_meta()}
  </head>

  <body align="center">

    <div class="outer-body">
      <div class="hjb-title">
	<h2 class="hjb-title">HJB (HTTP JMS Bridge)</h2>
      </div>

      <div class="navigation-bar">
	<table align="center" class="navigation-bar">
	  <tbody>
	    <tr>
	      <td><a href="index.html">Home</a></td>
	      <td><a href="http://hjb.berlios.de/download.html">Download</a></td>
	      <td><a href="http://hjb.berlios.de/doc.html">Documentation</a></td>
	      <td><a href="http://hjb.berlios.de/detailed-design.html">Design</a></td>
	      <td><a href="mailto:hjb-users@lists.berlios.de">Support</a></td>
	    </tr>
	  </tbody>
	</table>
      </div>
   
      <rst-body>Body to be generated by docutils</rst-body>
    </div>
   
  </body>
</html>
