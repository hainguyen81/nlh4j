<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/tags/Common.inc" %>

<td type="no">{{$index + 1}}</td>
<td type="no">{{data.code}}</td>
<td>{{data.name}}</td>
<td type="number">{{data.rolesCount}}</td>
<td>{{data.description}}</td>