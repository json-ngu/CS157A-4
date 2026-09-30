<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en" data-theme="dark">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script>
        (function () {
            var theme = "dark";

            try {
                if (localStorage.getItem("mm-theme") === "light") {
                    theme = "light";
                }
            } catch (e) {}

            document.documentElement.setAttribute("data-theme", theme);
        })();
    </script>

    <title>Register | MindMerge</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <style>
        .auth-page {
            max-width: 480px;
            margin: 80px auto;
            padding: 0 24px;
        }

        .auth-card {
            padding: 32px;
            border: 1px solid var(--line);
            border-radius: 12px;
        }

        .auth-card label {
            display: block;
            margin-top: 18px;
        }

        .auth-card input {
            width: 100%;
            box-sizing: border-box;
            padding: 12px;
            margin-top: 6px;
        }

        .auth-card button {
            margin-top: 24px;
        }

        .auth-error {
            color: var(--bad);
            margin: 16px 0;
        }
    </style>
</head>

<body>

<main class="auth-page">

    <div class="auth-card">

        <p class="eyebrow">MindMerge</p>

        <h1>Create an account</h1>

        <p>
            New accounts are regular team members for now.
        </p>

        <% if (request.getAttribute("error") != null) { %>
            <p class="auth-error">
                <%= request.getAttribute("error") %>
            </p>
        <% } %>

        <form method="post"
              action="${pageContext.request.contextPath}/register">

            <label for="displayName">Display name</label>

            <input id="displayName"
                   name="displayName"
                   type="text"
                   required
                   autocomplete="name">

            <label for="username">Username</label>

            <input id="username"
                   name="username"
                   type="text"
                   required
                   autocomplete="username">

            <label for="password">Password</label>

            <input id="password"
                   name="password"
                   type="password"
                   required
                   autocomplete="new-password">

            <label for="confirmPassword">
                Confirm password
            </label>

            <input id="confirmPassword"
                   name="confirmPassword"
                   type="password"
                   required
                   autocomplete="new-password">

            <button class="btn" type="submit">
                Register
            </button>
        </form>

        <p>
            Already have an account?
            <a href="${pageContext.request.contextPath}/login">
                Log in
            </a>
        </p>

        <p>
            <a href="${pageContext.request.contextPath}/home">
                Back to homepage
            </a>
        </p>

    </div>

</main>

</body>
</html>