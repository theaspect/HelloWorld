<%@ page import="helloworld.Hello" %>



<div class="fieldcontain ${hasErrors(bean: helloInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="hello.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${helloInstance?.title}"/>
</div>

