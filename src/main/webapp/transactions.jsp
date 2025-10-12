<%@ page import="com.package.Transaction" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Transaction History - XBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #D3DAD9; font-family: 'Segoe UI', sans-serif; }
        .navbar { border-bottom: 2px solid #ffc107; }
        .card { border-radius: 15px; border: 2px solid #ffc107; }
        table { margin-top: 20px; }
        th, td { text-align: center; }
        a.text-decoration-none { color: #fd7e14; font-weight: 500; }
        a.text-decoration-none:hover { color: #d35400; text-decoration: underline; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light ps-5">
    <div class="container-fluid">
        <a class="navbar-brand fs-4" href="#">X<span class="text-warning">Bank</span></a>
    </div>
</nav>

<div class="container mt-5">
    <div class="card shadow-lg p-4 mx-auto" style="max-width: 900px;">
        <h3 class="text-center mb-4 text-dark">Transaction History</h3>
        <table class="table table-striped">
            <thead class="table-warning">
                <tr>
                    <th>ID</th>
                    <th>Type </th>
                    <th> Amount ($)</th>
                    <th>Account Number</th>
                    <th>Timestamp</th>
                </tr>
            </thead>
            <tbody>
               <%
               List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
               if (transactions != null) {
                   for(Transaction t : transactions){
               %>
               <tr>
                   <td><%= t.getId() %></td>
                   <td><%= t.getType() %></td>
                   <td><%= t.getAmount() %></td>
                   <td><%= t.getAccountNumber() %></td>
                   <td><%= t.getTimestamp() %></td>
               </tr>
               <%
                   }
               } else {
               %>
               <tr><td colspan="5" class="text-center">No transactions found.</td></tr>
               <% } %>


            </tbody>
        </table>

        <div class="text-center mt-3">
            <a href="main.html" class="text-decoration-none"> Back to Dashboard</a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
