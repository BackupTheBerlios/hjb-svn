<?xml version='1.0' encoding='utf-8'?>
<?python?>
<html xmlns:py='http://purl.org/kid/ns#' py:layout="'layout.kid'">

  <link py:def="page_css" py:replace="XML(rst_stylesheet)"/>
  <meta py:def="page_meta" py:replace="XML(rst_meta)"/>
  <title py:match="item.tag == 'title'">${rst_title}</title>

  <div py:match="item.tag == 'rst-body'">
    
     <div class="inner-body">
      <h2>${rst_title}</h2>
      ${XML(rst_body)}
      ${XML(rst_footer)}
      <div class="small-print">
	Copyright &#169; 2006 Tim Emiola. All rights reserved.
      </div>
    </div>

  </div>

</html>
