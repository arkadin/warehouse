package controllers;

import static java.lang.String.format;

import java.util.Set;

import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.products.list;
import views.html.products.show;

public class Products extends Controller {

  private static final Form<Product> productForm = form(Product.class);

  public static Result list() {
    Set<Product> products = Product.findAll();
    return ok(list.render(products));
  }

  public static Result showBlank() {
    return ok(show.render(productForm));
  }

  public static Result show(Long ean) {
    final Product product = Product.findByEan(ean);
    if (product == null)
      return notFound(format("Product %s does not exist.", ean));

    Form<Product> filledForm = productForm.fill(product);
    return ok(show.render(filledForm));
  }

  public static Result save() {
    Form<Product> boundForm = productForm.bindFromRequest();
    if (boundForm.hasErrors()) {
      flash("error", "Please correct the form below.");
      return badRequest(show.render(boundForm));
    }

    Product product = boundForm.get();
    Product.add(product);
    flash("success",
        String.format("Successfully added product %s", product));

    return redirect(routes.Products.list());
  }

}
