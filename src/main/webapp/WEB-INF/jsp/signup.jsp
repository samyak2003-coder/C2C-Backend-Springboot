<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7fc;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .form-container {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }

        .form-container div {
            margin-bottom: 15px;
        }

        .form-container label {
            font-size: 14px;
            color: #555;
            display: block;
            margin-bottom: 5px;
        }

        .form-container input {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            outline: none;
        }

        .form-container input:focus {
            border-color: #66afe9;
        }

        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            font-size: 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .form-container button:hover {
            background-color: #45a049;
        }

        .form-container button:focus {
            outline: none;
        }

        .form-container p {
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
        }

        .form-container p a {
            color: #4CAF50;
            text-decoration: none;
        }

        .form-container p a:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>

    <div class="form-container">
        <h1>Sign Up</h1>
        <form action="http://localhost:8081/api/auth/sign-up" method="POST">
            <div>
                <label for="name">Full Name:</label>
                <input type="text" id="name" name="name" required />
            </div>

            <div>
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required />
            </div>

            <div>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required />
            </div>

            <button type="submit">Sign Up</button>
        </form>

        <p>Already have an account? <a href="/login">Login here</a></p>
    </div>

</body>
</html>