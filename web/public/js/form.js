/**
 * Created by lookitsmarc on 15/05/2017.
 */
const formFactory = (() => {

    const fields = Object.assign({}, {
        pwd: `
            <div class="input-group pwd" >
              <span class="input-group-addon" id="password">Password</span>
              <input type="text" class="form-control" placeholder="" name="password" aria-describedby="password">
            </div>
        `,
        mail: `
            <div class="input-group mail">
                <span class="input-group-addon" id="mail">Mail</span>
                <input type="text" class="form-control" placeholder="" name="mail" aria-describedby="mail">
            </div>`,
        date: `
            <div class="form-group row date">
              <label for="example-datetime-local-input" class="col-2 col-form-label">Date and time</label>
              <div class="col-10">
                <input class="form-control" type="date" value="" id="example-datetime-local-input" name="start_date">
              </div>
            </div>
            <div class="form-group row date">
              <label for="example-datetime-local-input" class="col-2 col-form-label">Date and time</label>
              <div class="col-10">
                <input class="form-control" type="date" value="" id="example-datetime-local-input" name="end_date">
              </div>
            </div>
        `,
        captcha: `
             <div class="form-check">
                <label class="form-check-label">
                    <input type="radio" class="form-check-input" name="captcha" id="optionsRadios2" value="enable">
                    Enable captcha
                </label>
            </div>
        `,
        pwds: `
            <div class="input-group pwd" >
              <span class="input-group-addon" id="password">Password 1</span>
              <input type="text" class="form-control" placeholder="" name="passwords-1" aria-describedby="password">
            </div>
            <div class="input-group pwd" >
              <span class="input-group-addon" id="password">Password 2</span>
              <input type="text" class="form-control" placeholder="" name="passwords-2" aria-describedby="password">
            </div>
        `
    });

    /**
     * Add Component
     * @param {String} type
     */
    const addComponent = (type, DOMBind) => {
        console.log(fields[type]);
        document.getElementById(DOMBind).insertAdjacentHTML('afterbegin', fields[type]);
    };

    /**
     * Rm Component
     * @param {String} type
     */
    const rmComponent = (target) => {
        let rmtarg = document.getElementsByClassName(target);
        
        rmtarg.map(DOMElement => {
            DOMElement.remove();
        });
    };

    /**
     * Init Component
     */
    const initComponent = () => {
        document.addEventListener('DOMContentLoaded', () => {
            let DOMLists = document.getElementsByClassName('addon');

            for (let list of DOMLists) {
                list.addEventListener('click', addComponent.bind(null, list.getAttribute('data-id'), 'urlform'));
            }
        });
    };

    // Init the universe
    initComponent();

    return {
        addcomponent: addComponent,
        rmcomponent: rmComponent,
    }
})();