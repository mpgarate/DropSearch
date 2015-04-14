require 'spec_helper'

describe Page, "" do
  it "gets next urls" do
    url = "http://en.wikipedia.org/wiki/Albert_Gallatin"

    # fake the crawl response

    body = %{
      <a href="http://en.wikipedia.org/wiki/Alexander_Hamilton">
      <a href="http://www.questia.com/PM.qst?a=o&d=91316346">
      <a href="http://en.wikipedia.org/wiki/Albert_Gallatin#mw-head">
      <a href="http://en.wikipedia.org/wiki/File:AlbertGallatin.jpeg">
      <a href="mailto:foo@example.com">
    }
    page = Page.new(url: url, body: body)

    # includes a good url
    expect(page.next_urls).to include("http://en.wikipedia.org/wiki/Alexander_Hamilton")

    # omits urls from other domains
    expect(page.next_urls).not_to include("http://www.questia.com/PM.qst?a=o&d=91316346")

    # omits urls to the same page
    expect(page.next_urls).not_to include("http://en.wikipedia.org/wiki/Albert_Gallatin#mw-head")

    # omits urls to jpeg extension
    expect(page.next_urls).not_to include("http://en.wikipedia.org/wiki/File:AlbertGallatin.jpeg")

    # omits mailto links
    expect(page.next_urls).not_to include("mailto:foo@example.com")

  end

  it "creates keywords" do
    url = "http://en.wikipedia.org/wiki/Albert_Gallatin"

    body = %{
      <a href="http://en.wikipedia.org/wiki/Alexander_Hamilton">
      <a href="http://www.questia.com/PM.qst?a=o&d=91316346">
      <a href="http://en.wikipedia.org/wiki/Albert_Gallatin#mw-head">
      <a href="http://en.wikipedia.org/wiki/File:AlbertGallatin.jpeg">
      <a href="mailto:foo@example.com">

      <p>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent
        a elit ut nulla luctus aliquet. Phasellus imperdiet dolor massa.
        Phasellus blandit pellentesque sapien, nec luctus justo ultrices
        vitae. Vivamus volutpat sed tellus quis hendrerit. Aliquam lacinia
        condimentum imperdiet. Proin sit amet ex sapien. Aliquam libero quam,
        pretium quis accumsan id, porttitor id est.
      </p>
    }

    page = Page.new(url: url, body: body)
    page.process_words!


    expect(page.keywords).to include(Keyword.new(term: "consectetur"))
  end
end