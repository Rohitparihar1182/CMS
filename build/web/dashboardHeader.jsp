<div class="modal-container hidden">
    <div class="shadow"></div>
    <div class="modal">
        <form action="DeleteAccount" method="POST">
            <div>
                <label class="required-label" for="password">Confirm Password(uid = <%= request.getAttribute("uid") %>)</label>
                <input required type="password" name="password" id="password" />
            </div>
            <div class="form-btn-container">
                <button id="close-modal" type="button">
                    Cancel
                </button>
                <button type="submit">
                    Delete
                </button>
            </div>
            
        </form>
    </div>
</div>
<header>
    <div class="container">
        <div class="header-content">
            <div class="header-content-left">
                <div>
                    <a href="/CMS" class="title">CMS</a>
                </div>
                <div>
                    <input type="text" class="search">
                </div>
            </div>
            <nav class="primary-navigations">
                <ul>
                    <li style="display: flex; align-items: center;" id="toggle-theme"><span>Toggle Theme</span></li>
                    <li><a class="btn" href="/CMS/Logout" target="_top">Logout</a></li>
                </ul>
            </nav>
            <div class="nav-trigger">
                <img src="./assets/menu.png" alt="ham">
            </div>
        </div>
    </div>
</header>
<script src="./js/header.js"></script>