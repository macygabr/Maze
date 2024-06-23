
  document.addEventListener('DOMContentLoaded', function() {
    const currentUrl = window.location.pathname;

    const navLinks = [
      { id: 'home-link', href: '/' },
      { id: 'field-link', href: '/field' },
      { id: 'files-link', href: '/files' },
      { id: 'about-link', href: '/about' }
    ];

    function updateNavLinks() {
      navLinks.forEach(link => {
        const element = document.getElementById(link.id);
        if (currentUrl === link.href) {
          element.classList.remove('text-white');
          element.classList.add('text-secondary');
        } else {
          element.classList.remove('text-secondary');
          element.classList.add('text-white');
        }
      });
    }

    updateNavLinks();
    document.getElementById('user-login').textContent = server.user.login;


    // var rebootElement = document.getElementById("reboot-li");
    // var helpElement = document.getElementById("help-li");
    // var uploadElement = document.getElementById("upload-li");
    // var saveElement = document.getElementById("save-li");
    // if (currentUrl.includes("/field")) {
    //     rebootElement.style.display = "list-item";
    //     helpElement.style.display = "list-item";
    //     saveElement.style.display = "save-item";
    //     uploadElement.style.display = "list-item";
    //   } else {
    //     rebootElement.style.display = "none";
    //     helpElement.style.display = "none";
    //     saveElement.style.display = "none";
    //     uploadElement.style.display = "none";
    // }
  });
  

  if(server.user.authentication) {
    document.getElementById('text-end-element').hidden = true;
    document.getElementById('profile-element').hidden = false;
  }
  else {
    document.getElementById('text-end-element').hidden = false;
    document.getElementById('profile-element').hidden = true;
  }